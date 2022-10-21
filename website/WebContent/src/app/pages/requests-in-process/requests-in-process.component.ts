import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import * as shape from 'd3-shape';
import { Subject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { UserApiService } from '../../generated/api/service-requester/api/userApi.service';
import { Node, Link } from '../../services/d3';
import { MessageService } from '../../services/messages/messages.service';
import { LoadingService } from '../../services/loading/loading.service';
import { GraphComponent } from '../../components/visuals/graph/graph.component';
import * as d3 from 'd3';
import * as moment from 'moment';

interface JobstateAndRequest {
  requestName?: string;
  requestUUID?: string;
  jobstate?: string;
  steps?: Array<Step>;
}

interface Step {
  id?: string;
  title?: string;
  type?: string;
  status?: string;
  extra?: string;
}

interface Source {
  [x: string]: any;
  nodeid?: string;
  fromnodeid?: string;
  tonodeid?: string;
  elasticID?: string;
  nodeType?: string;
  configurationLabel?: string;
}

interface Hit {
  _id?: string;
  _index?: string;
  _type?: string;
  _score?: number;
  _source?: Source;
}

interface Hits {
  total?: string;
  max_score?: string;
  hits?: Array<Hit>;
}

interface ESI {
  _shards?: object;
  hits?: Hits;
  took?: number;
  timed_out?: boolean;
}


@Component({
  selector: 'app-requests-in-process',
  templateUrl: './requests-in-process.component.html',
  styleUrls: ['./requests-in-process.component.sass']
})

export class RequestsInProcessComponent implements OnInit, OnDestroy {
  @ViewChild('graph') graph: GraphComponent;
  @ViewChild('step') stepIndexInput: any;

  constructor(
    private http: HttpClient,
    private loading: LoadingService,
    private message: MessageService,
    private router: Router,
    private route: ActivatedRoute,
    private userApiService: UserApiService,
  ) { }
  scale = 60;
  timeInterval = 2500;
  requestUUID: string;
  request: any;
  requestCreatedAt = 0;
  dataSource: Array<JobstateAndRequest>;
  selectedItem: JobstateAndRequest;
  checked: boolean;
  requestInProgress: boolean;
  result = {};
  error = {};
  graphVisibility = localStorage.graph !== undefined ? JSON.parse(localStorage.getItem('graph')) : true;
  now = Date.now();
  counter = '';

  steps: Array<Step> = [
    { id: 'START', title: 'Start', type: 'point', status: '', extra: '' },
    { id: 'PREPARING', title: 'Preparation', type: 'job', status: 'pending', extra: '' },
    { id: 'CONFIGURING', title: 'Configuration', type: 'job', status: 'pending', extra: '' },
    { id: 'BUILDING', title: 'Build', type: 'job', status: 'pending', extra: '' },
    { id: 'PUBLISHING', title: 'Publish', type: 'job', status: 'pending', extra: '' },
    { id: 'END', title: 'End', type: 'point', status: '', extra: '' },
  ];

  offers = [''];
  timer: any;
  nowTimer: any;
  nodes: Node[] = [];
  links: Link[] = [];

  ngOnInit() {
    this.loadData();
    this.calculateCounter();
    this.requestUUID = this.route.snapshot.paramMap.get('id') || '';
  }

  ngOnDestroy() {
    clearInterval(this.timer);
    clearInterval(this.nowTimer);
  }

  getOffersStatus = () => {
    if (this.selectedItem.jobstate !== 'DONE') {
      this.timer = setTimeout(() => {
        this.userApiService.getJobStateForOTFProvider(this.selectedItem.requestUUID).subscribe(response => {
          this.selectedItem.jobstate = response;
          this.calculateSteps();
        });
      }, this.timeInterval);
    }
  }

  loadData = () => {
    this.loading.show();
    this.dataSource = [];
    this.userApiService.getAllJobStates().subscribe(response => {
      if (response.length) {
        this.dataSource = response;
        this.selectedItem = response.filter(x => x.requestUUID === this.requestUUID)[0] || response[response.length - 1];
        this.calculateSteps();
      }
      this.checked = true;
      this.loading.hide();
    }, error => {
      this.loading.hide();
      this.message.error(error.message);
    });
  }

  onChange = () => {
    this.message.clear();
    const path = this.route.snapshot.routeConfig.path.split('/')[0];
    if (path) {
      this.router.navigate([`/${path}/${this.selectedItem.requestUUID}`]);
      this.resetGraph();
      this.calculateSteps();
    }
  }

  getESData = () => {
    this.userApiService.getMonitorSourcesForRequest(this.selectedItem.requestUUID).subscribe(urls => {
      // Getting Nodes
      this.http.get(urls.nodeSupplierURL).subscribe(nResponse => {
        const nodesResponse = <ESI>nResponse;
        if (nodesResponse.hits && nodesResponse.hits.hits.length) {
          nodesResponse.hits.hits.map(x => {
            if (x._source.nodeid === 'node0') {
              this.requestCreatedAt = Date.parse(x._source.creationDate);
              console.log(this.requestCreatedAt);
            }
            const node = new Node(x._source.nodeid, x._source.nodeType, x._source.configurationLabel);
            this.pushNode(node);
          });
          // Getting Edges
          this.http.get(urls.edgeSupplierURL).subscribe(eReponse => {
            const edgesReponse = <ESI>eReponse;
            if (edgesReponse.hits && edgesReponse.hits.hits.length) {
              edgesReponse.hits.hits.map(x => {
                this.pushLink(x._source.fromnodeid, x._source.tonodeid);
              });
            }
          }); // end Getting Edges
        }
      }); // end Getting Nodes

    });
  }

  getNode = (node) => this.nodes.find(x => x.id === node);
  getNodeIndex = (node) => this.graph.graph.nodes.findIndex(x => x.id === node.id);
  getLink = (s, t) => this.graph.graph.links.find(x => x.source.id === s && x.target.id === t);

  pushLink(source, target) {
    if (!this.getLink(source, target)) {
      const sourceNode = this.getNode(source);
      const targetNode = this.getNode(target);
      if (sourceNode && targetNode) {
        const link = new Link(sourceNode, targetNode);
        this.graph.graph.links.push(link);
        this.restartGraph();
      }
    }
  }

  pushNode(node) {
    const nodeIndex = this.getNodeIndex(node);
    if (nodeIndex > -1) {
      this.graph.graph.nodes[nodeIndex].label = node.label;
    } else {
      this.graph.graph.nodes.push(node);
      this.restartGraph();
    }
  }


  restartGraph() {
    this.graph.graph.simulation.nodes(this.graph.graph.nodes);
    this.graph.graph.simulation.force('link', d3.forceLink(this.graph.graph.links).distance(25));
    this.graph.graph.simulation.alpha(1).restart();
  }

  resetGraph() {
    this.nodes = this.graph.graph.nodes = [];
    this.links = this.graph.graph.links = [];
    this.restartGraph();
  }

  getStepIndex = (id: string) => {
    return this.steps.findIndex(x => x.id === id);
  }

  calculateSteps = () => {
    // get a clone from steps by value
    this.selectedItem.steps = JSON.parse(JSON.stringify(this.steps));
    let stepIndex = this.getStepIndex(this.selectedItem.jobstate);

    if (stepIndex < 0 && this.selectedItem.jobstate !== 'DONE') {
      return false;
    }

    // change from DONE to PUBLISHING
    if (this.selectedItem.jobstate === 'DONE') {
      stepIndex = 4;
    }

    this.selectedItem.steps.forEach((x, i) => x.status = i < stepIndex ? 'done' : 'pending');

    if (this.selectedItem.jobstate === 'DONE') {
      this.selectedItem.steps[stepIndex].status = 'done';
      this.selectedItem.steps[stepIndex].extra = 'final';
    } else {
      this.selectedItem.steps[stepIndex].status = 'doing';
    }

    this.getOffersStatus();
    this.getESData();
    this.extractInformation();
  }

  getFlushURL(url) {
    const urlObj = new URL(url);
    return urlObj.origin + urlObj.pathname.replace('search', 'flush');
  }

  getRefreshURL(url) {
    const urlObj = new URL(url);
    return urlObj.origin + urlObj.pathname.replace('search', 'refresh');
  }

  zoom(v) {
    if (this.scale < 10 && v < 0) {
      return false;
    }
    this.scale += v;
  }

  zoomIn() {
    this.zoom(5);
  }

  zoomOut() {
    this.zoom(-5);
  }

  toggleGraph() {
    this.graphVisibility = !this.graphVisibility;
    localStorage.setItem('graph', JSON.stringify(this.graphVisibility));
  }

  extractInformation() {
    this.userApiService.getExtractedInformation(this.selectedItem.requestUUID).subscribe(response => {
      this.request = response;
    });
  }

  calculateCounter() {
    this.nowTimer = setInterval(() => {
      if (this.request !== undefined && this.request.timeout !== undefined && this.requestCreatedAt) {
        this.now = Date.now();
        // const c = this.now - (parseInt(this.request.timeout, 0) * 1000 + this.requestCreatedAt);
        const c = parseInt(this.request.timeout, 0) * 1000 - (this.now - this.requestCreatedAt);

        if (c >= 0) {
          this.counter = moment.utc(moment.duration(Math.abs(c)).asMilliseconds()).format('HH:mm:ss');
        }

      }
    }, 1000);
  }

}

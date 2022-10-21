// tslint:disable-next-line:max-line-length
import { Component, Input, ChangeDetectorRef, HostListener, ChangeDetectionStrategy, OnInit, AfterViewInit, ViewChild, ElementRef } from '@angular/core';
import { D3Service, ForceDirectedGraph, Node } from '../../../services/d3';

@Component({
  selector: 'app-graph',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './graph.component.html',
  styleUrls: ['./graph.component.sass']
})

export class GraphComponent implements OnInit, AfterViewInit {
  @Input() nodes = [];
  @Input() links = [];
  @Input() scale: any;
  @Input() visibility = true;
  @ViewChild('box') box: ElementRef;
  @HostListener('window:resize', ['$event'])

  translate = 250;
  chartScale = 1;
  graph: ForceDirectedGraph;
  _options = { width: 800, height: 600 };


  onResize(event) {
    this.graph.initSimulation(this.options);
  }

  constructor(
    public d3Service: D3Service,
    private ref: ChangeDetectorRef
  ) { }

  ngOnInit() {
    // Receiving an initialized simulated graph from our custom d3 service
    this.graph = this.d3Service.getForceDirectedGraph(this.nodes, this.links, this.options);

    // Binding change detection check on each tick
    // This along with an onPush change detection strategy should enforce checking only when relevant!
    // This improves scripting computation duration in a couple of tests I've made, consistently.
    // Also, it makes sense to avoid unnecessary checks when we are dealing only with simulations data binding.

    this.graph.ticker.subscribe((d) => {
      this.ref.markForCheck();
    });
  }

  ngAfterViewInit() {
    this.graph.initSimulation(this.options);
  }

  get options() {
    const width = window.getComputedStyle(document.getElementById('content')).width;
    return this._options = {
      width: parseInt(width, 0) - 80,
      height: parseInt(width, 0) - 80,
    };
  }

}

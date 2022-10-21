import { Component, ViewChild, AfterViewInit } from '@angular/core';
import { Link, Node } from '../../services/d3/models';
import { GraphComponent } from '../../components/visuals/graph/graph.component';
import * as d3 from 'd3';

class TypedLink extends Link {

  constructor(source: Node, target: Node, type: string) {
    super(source, target);
    this.type = type;
  }

  private type: string;

}

@Component({
  selector: 'app-otf-provider-network',
  templateUrl: './otf-provider-network.component.html',
  styleUrls: ['./otf-provider-network.component.sass']
})

export class OtfProviderNetworkComponent implements AfterViewInit {
  @ViewChild('graph') graph: GraphComponent;
  nodes: Node[] = [];
  links: TypedLink[] = [];
  linksMap = {};
  scale = 60;

  log = [
    [1544195590416, 'NEW_EDGE', '0', 'MP', '-65536', 'SUB_SUP'],
    [1544195590417, 'UPDATE_NODE_LABEL', '0', '0'],
    [1544195591872, 'NEW_EDGE', '1', 'MP', '-65536', 'SUB_SUP'],
    [1544195591872, 'UPDATE_NODE_LABEL', '1', '1'],
    [1544195591872, 'NEW_EDGE', '2', 'MP', '-65536', 'SUB_SUP'],
    [1544195591872, 'UPDATE_NODE_LABEL', '2', '2'],
    [1544195591873, 'NEW_EDGE', '3', 'MP', '-65536', 'SUB_SUP'],
    [1544195591873, 'UPDATE_NODE_LABEL', '3', '3'],
    [1544195591873, 'NEW_EDGE', '4', 'MP', '-65536', 'SUB_SUP'],
    [1544195591873, 'UPDATE_NODE_LABEL', '4', '4'],
    [1544195591874, 'NEW_EDGE', '5', 'MP', '-65536', 'SUB_SUP'],
    [1544195591874, 'UPDATE_NODE_LABEL', '5', '5'],
    [1544195591874, 'NEW_EDGE', '6', 'MP', '-65536', 'SUB_SUP'],
    [1544195591874, 'UPDATE_NODE_LABEL', '6', '6'],
    [1544195591874, 'NEW_EDGE', '7', 'MP', '-65536', 'SUB_SUP'],
    [1544195591875, 'UPDATE_NODE_LABEL', '7', '7'],
    [1544195591875, 'NEW_EDGE', '8', 'MP', '-65536', 'SUB_SUP'],
    [1544195591875, 'UPDATE_NODE_LABEL', '8', '8'],
    [1544195591875, 'NEW_EDGE', '9', 'MP', '-65536', 'SUB_SUP'],
    [1544195591876, 'UPDATE_NODE_LABEL', '9', '9'],
    [1544195591876, 'NEW_EDGE', '10', 'MP', '-65536', 'SUB_SUP'],
    [1544195591876, 'UPDATE_NODE_LABEL', '10', '10'],
    [1544195591876, 'NEW_EDGE', 'MP', '0', '-65536', 'SUP_EDGE'],
    [1544195591876, 'NEW_EDGE', '11', 'MP', '-65536', 'SUB_SUP'],
    [1544195591876, 'UPDATE_NODE_LABEL', '11', '11'],
    [1544195591883, 'NEW_EDGE', '12', 'MP', '-65536', 'SUB_SUP'],
    [1544195591884, 'UPDATE_NODE_LABEL', '12', '12'],
    [1544195591884, 'NEW_EDGE', '13', 'MP', '-65536', 'SUB_SUP'],
    [1544195591884, 'UPDATE_NODE_LABEL', '13', '13'],
    [1544195591884, 'NEW_EDGE', '14', 'MP', '-65536', 'SUB_SUP'],
    [1544195591885, 'UPDATE_NODE_LABEL', '14', '14'],
    [1544195591885, 'NEW_EDGE', '15', 'MP', '-65536', 'SUB_SUP'],
    [1544195591885, 'UPDATE_NODE_LABEL', '15', '15'],
    [1544195591885, 'NEW_EDGE', 'MP', '1', '-65536', 'SUP_EDGE'],
    [1544195591885, 'NEW_EDGE', '16', 'MP', '-65536', 'SUB_SUP'],
    [1544195591885, 'UPDATE_NODE_LABEL', '16', '16'],
    [1544195591886, 'NEW_EDGE', '17', 'MP', '-65536', 'SUB_SUP'],
    [1544195591886, 'UPDATE_NODE_LABEL', '17', '17'],
    [1544195591886, 'NEW_EDGE', '18', 'MP', '-65536', 'SUB_SUP'],
    [1544195591886, 'UPDATE_NODE_LABEL', '18', '18'],
    [1544195591901, 'NEW_EDGE', 'MP', '2', '-65536', 'SUP_EDGE'],
    [1544195591901, 'NEW_EDGE', '19', 'MP', '-65536', 'SUB_SUP'],
    [1544195591902, 'UPDATE_NODE_LABEL', '19', '19'],
    [1544195591903, 'NEW_EDGE', '20', 'MP', '-65536', 'SUB_SUP'],
    [1544195591903, 'UPDATE_NODE_LABEL', '20', '20'],
    [1544195591903, 'NEW_EDGE', 'MP', '3', '-65536', 'SUP_EDGE'],
    [1544195591903, 'NEW_EDGE', '21', 'MP', '-65536', 'SUB_SUP'],
    [1544195591903, 'UPDATE_NODE_LABEL', '21', '21'],
    [1544195591912, 'NEW_EDGE', '22', 'MP', '-65536', 'SUB_SUP'],
    [1544195591912, 'UPDATE_NODE_LABEL', '22', '22'],
    [1544195591912, 'NEW_EDGE', 'MP', '4', '-65536', 'SUP_EDGE'],
    [1544195591916, 'NEW_EDGE', 'MP', '5', '-65536', 'SUP_EDGE'],
    [1544195591918, 'NEW_EDGE', '1', '0', '-16776961', 'CYCLE_LEFT'],
    [1544195591918, 'NEW_EDGE', '5', '2', '-16776961', 'CYCLE_LEFT'],
    [1544195591918, 'NEW_EDGE', '1', '0', '-16776961', 'CYCLE_CYCLE'],
    [1544195591919, 'NEW_EDGE', 'MP', '6', '-65536', 'SUP_EDGE'],
    [1544195591912, 'NEW_EDGE', '23', 'MP', '-65536', 'SUB_SUP'],
    [1544195591918, 'NEW_EDGE', '5', '1', '-16776961', 'CYCLE_RIGHT'],
    [1544195591918, 'NEW_EDGE', '2', '0', '-16776961', 'CYCLE_LEFT'],
    [1544195591918, 'NEW_EDGE', '4', '0', '-16776961', 'CYCLE_LEFT'],
    [1544195591918, 'NEW_EDGE', '3', '1', '-16776961', 'CYCLE_LEFT'],
    [1544195591920, 'NEW_EDGE', '4', '2', '-16776961', 'CYCLE_RIGHT'],
    [1544195591919, 'NEW_EDGE', '2', '1', '-16776961', 'CYCLE_RIGHT'],
    [1544195591919, 'UPDATE_NODE_LABEL', '23', '23'],
    [1544195591920, 'NEW_EDGE', 'MP', '7', '-65536', 'SUP_EDGE'],
    [1544195591920, 'NEW_EDGE', '6', '1', '-16776961', 'CYCLE_LEFT'],
    [1544195591920, 'NEW_EDGE', '3', '0', '-16776961', 'CYCLE_CYCLE'],
    [1544195591920, 'NEW_EDGE', '24', 'MP', '-65536', 'SUB_SUP'],
    [1544195591920, 'NEW_EDGE', '6', '3', '-16776961', 'CYCLE_RIGHT'],
    [1544195591921, 'NEW_EDGE', '0', '1', '-16776961', 'CYCLE_CYCLE'],
    [1544195591921, 'REMOVE_EDGE', '1', '0', '-16776961', 'CYCLE_LEFT'],
    [1544195591921, 'NEW_EDGE', '7', '3', '-16776961', 'CYCLE_LEFT'],
    [1544195591920, 'UPDATE_NODE_LABEL', '24', '24'],
    [1544195591921, 'NEW_EDGE', '1', '5', '-16776961', 'CYCLE_LEFT'],
    [1544195591921, 'NEW_EDGE', '7', '0', '-16776961', 'CYCLE_CYCLE'],
    [1544195591921, 'NEW_EDGE', 'MP', '8', '-65536', 'SUP_EDGE'],
    [1544195591921, 'NEW_EDGE', '25', 'MP', '-65536', 'SUB_SUP'],
    [1544195591922, 'UPDATE_NODE_LABEL', '25', '25'],
    [1544195591922, 'NEW_EDGE', '8', '0', '-16776961', 'CYCLE_LEFT'],
    [1544195591922, 'NEW_EDGE', '26', 'MP', '-65536', 'SUB_SUP'],
    [1544195591922, 'NEW_EDGE', '8', '4', '-16776961', 'CYCLE_RIGHT'],
    [1544195591922, 'UPDATE_NODE_LABEL', '26', '26'],
    [1544195591953, 'NEW_EDGE', '27', 'MP', '-65536', 'SUB_SUP'],
    [1544195591953, 'REMOVE_EDGE', '2', '1', '-16776961', 'CYCLE_RIGHT'],
    [1544195591953, 'NEW_EDGE', '2', '5', '-16776961', 'CYCLE_RIGHT'],
    [1544195591953, 'UPDATE_NODE_LABEL', '27', '27'],
    [1544195591961, 'NEW_EDGE', '3', '7', '-16776961', 'CYCLE_RIGHT'],
    [1544195591961, 'NEW_EDGE', '28', 'MP', '-65536', 'SUB_SUP'],
    [1544195591961, 'UPDATE_NODE_LABEL', '28', '28'],
    [1544195591961, 'REMOVE_EDGE', '4', '0', '-16776961', 'CYCLE_LEFT'],
    [1544195591962, 'NEW_EDGE', '29', 'MP', '-65536', 'SUB_SUP'],
    [1544195591962, 'UPDATE_NODE_LABEL', '29', '29'],
    [1544195591962, 'NEW_EDGE', '30', 'MP', '-65536', 'SUB_SUP'],
    [1544195591962, 'REMOVE_EDGE', '2', '0', '-16776961', 'CYCLE_LEFT'],
    [1544195591962, 'NEW_EDGE', '2', '4', '-16776961', 'CYCLE_LEFT'],
    [1544195591960, 'NEW_EDGE', '0', '1', '-16776961', 'CYCLE_RIGHT'],
    [1544195591961, 'NEW_EDGE', '4', '8', '-16776961', 'CYCLE_LEFT'],
    [1544195591962, 'REMOVE_EDGE', '3', '0', '-16776961', 'CYCLE_CYCLE'],
    [1544195591980, 'REMOVE_EDGE', '3', '1', '-16776961', 'CYCLE_LEFT'],
    [1544195591980, 'NEW_EDGE', '3', '6', '-16776961', 'CYCLE_LEFT'],
    [1544195591962, 'UPDATE_NODE_LABEL', '30', '30'],
    [1544195591962, 'NEW_EDGE', '1', '3', '-16776961', 'CYCLE_RIGHT'],
    [1544195591981, 'NEW_EDGE', '31', 'MP', '-65536', 'SUB_SUP'],
    [1544195591981, 'UPDATE_NODE_LABEL', '31', '31'],
    [1544195591981, 'REMOVE_EDGE', '0', '1', '-16776961', 'CYCLE_RIGHT'],
    [1544195591982, 'NEW_EDGE', '0', '4', '-16776961', 'CYCLE_RIGHT'],
    [1544195591980, 'NEW_EDGE', 'MP', '9', '-65536', 'SUP_EDGE'],
    [1544195591983, 'REMOVE_EDGE', '1', '0', '-16776961', 'CYCLE_CYCLE'],
    [1544195591985, 'NEW_EDGE', '9', '4', '-16776961', 'CYCLE_LEFT'],
    [1544195591985, 'NEW_EDGE', '9', '2', '-16776961', 'CYCLE_RIGHT'],
    [1544195591985, 'NEW_EDGE', 'MP', '10', '-65536', 'SUP_EDGE'],
    [1544195591987, 'NEW_EDGE', '10', '2', '-16776961', 'CYCLE_LEFT'],
    [1544195591987, 'NEW_EDGE', '10', '5', '-16776961', 'CYCLE_RIGHT'],
    [1544195591987, 'REMOVE_EDGE', '0', '1', '-16776961', 'CYCLE_CYCLE'],
    [1544195591987, 'NEW_EDGE', '0', '3', '-16776961', 'CYCLE_CYCLE'],
    [1544195591990, 'REMOVE_EDGE', '1', '3', '-16776961', 'CYCLE_RIGHT'],
    [1544195591990, 'NEW_EDGE', '1', '6', '-16776961', 'CYCLE_RIGHT'],
    [1544195591997, 'REMOVE_EDGE', '0', '3', '-16776961', 'CYCLE_CYCLE'],
    [1544195591997, 'NEW_EDGE', '0', '7', '-16776961', 'CYCLE_CYCLE'],
    [1544195592004, 'REMOVE_EDGE', '7', '3', '-16776961', 'CYCLE_LEFT'],
    [1544195592005, 'NEW_EDGE', '7', '3', '-16776961', 'CYCLE_LEFT'],
    [1544195592005, 'REMOVE_EDGE', '0', '4', '-16776961', 'CYCLE_RIGHT'],
    [1544195592005, 'NEW_EDGE', '0', '8', '-16776961', 'CYCLE_RIGHT'],
    [1544195592009, 'REMOVE_EDGE', '2', '4', '-16776961', 'CYCLE_LEFT'],
    [1544195592009, 'NEW_EDGE', '2', '9', '-16776961', 'CYCLE_LEFT'],
    [1544195592014, 'REMOVE_EDGE', '4', '2', '-16776961', 'CYCLE_RIGHT'],
    [1544195592014, 'NEW_EDGE', '4', '9', '-16776961', 'CYCLE_RIGHT'],
    [1544195592019, 'REMOVE_EDGE', '5', '2', '-16776961', 'CYCLE_LEFT'],
    [1544195592019, 'NEW_EDGE', '5', '10', '-16776961', 'CYCLE_LEFT'],
    [1544195592022, 'REMOVE_EDGE', '7', '0', '-16776961', 'CYCLE_CYCLE'],
    [1544195592039, 'REMOVE_EDGE', '2', '5', '-16776961', 'CYCLE_RIGHT'],
    [1544195592039, 'NEW_EDGE', '2', '10', '-16776961', 'CYCLE_RIGHT'],
    [1544195592042, 'NEW_EDGE', 'MP', '11', '-65536', 'SUP_EDGE'],
    [1544195592042, 'NEW_EDGE', '1', '2', '-16777092', 'SHORTCUT'],
    [1544195592044, 'NEW_EDGE', '11', '5', '-16776961', 'CYCLE_LEFT'],
    [1544195592044, 'NEW_EDGE', '11', '1', '-16776961', 'CYCLE_RIGHT'],
    [1544195592044, 'NEW_EDGE', '7', '0', '-16776961', 'CYCLE_CYCLE'],
    [1544195592045, 'NEW_EDGE', 'MP', '12', '-65536', 'SUP_EDGE'],
    [1544195592045, 'NEW_EDGE', '12', '1', '-16776961', 'CYCLE_LEFT'],
    [1544195592045, 'NEW_EDGE', '12', '6', '-16776961', 'CYCLE_RIGHT'],
    [1544195592047, 'NEW_EDGE', 'MP', '13', '-65536', 'SUP_EDGE'],
    [1544195592048, 'NEW_EDGE', '13', '6', '-16776961', 'CYCLE_LEFT'],
    [1544195592048, 'NEW_EDGE', '13', '3', '-16776961', 'CYCLE_RIGHT'],
    [1544195592048, 'NEW_EDGE', 'MP', '14', '-65536', 'SUP_EDGE'],
    [1544195592048, 'NEW_EDGE', '14', '3', '-16776961', 'CYCLE_LEFT'],
    [1544195592048, 'NEW_EDGE', '14', '7', '-16776961', 'CYCLE_RIGHT'],
    [1544195592050, 'NEW_EDGE', 'MP', '15', '-65536', 'SUP_EDGE'],
    [1544195592051, 'NEW_EDGE', '15', '7', '-16776961', 'CYCLE_LEFT'],
    [1544195592051, 'NEW_EDGE', '15', '0', '-16776961', 'CYCLE_CYCLE'],
    [1544195592053, 'NEW_EDGE', 'MP', '16', '-65536', 'SUP_EDGE'],
    [1544195592053, 'NEW_EDGE', '16', '0', '-16776961', 'CYCLE_LEFT'],
    [1544195592053, 'NEW_EDGE', '16', '8', '-16776961', 'CYCLE_RIGHT'],
    [1544195592054, 'NEW_EDGE', '1', '0', '-16777092', 'SHORTCUT'],
    [1544195592054, 'NEW_EDGE', 'MP', '17', '-65536', 'SUP_EDGE'],
    [1544195592054, 'NEW_EDGE', '17', '8', '-16776961', 'CYCLE_LEFT'],
    [1544195592054, 'NEW_EDGE', '17', '4', '-16776961', 'CYCLE_RIGHT'],
    [1544195592055, 'NEW_EDGE', 'MP', '18', '-65536', 'SUP_EDGE'],
    [1544195592055, 'NEW_EDGE', '18', '4', '-16776961', 'CYCLE_LEFT'],
    [1544195592056, 'NEW_EDGE', '18', '9', '-16776961', 'CYCLE_RIGHT'],
    [1544195592057, 'NEW_EDGE', 'MP', '19', '-65536', 'SUP_EDGE'],
    [1544195592059, 'NEW_EDGE', '19', '9', '-16776961', 'CYCLE_LEFT'],
    [1544195592059, 'NEW_EDGE', '19', '2', '-16776961', 'CYCLE_RIGHT'],
    [1544195592059, 'NEW_EDGE', 'MP', '20', '-65536', 'SUP_EDGE'],
    [1544195592060, 'NEW_EDGE', '20', '2', '-16776961', 'CYCLE_LEFT'],
    [1544195592060, 'NEW_EDGE', '20', '10', '-16776961', 'CYCLE_RIGHT'],
    [1544195592062, 'NEW_EDGE', 'MP', '21', '-65536', 'SUP_EDGE'],
    [1544195592062, 'NEW_EDGE', '21', '10', '-16776961', 'CYCLE_LEFT'],
    [1544195592063, 'NEW_EDGE', '21', '5', '-16776961', 'CYCLE_RIGHT'],
    [1544195592064, 'NEW_EDGE', 'MP', '22', '-65536', 'SUP_EDGE'],
    [1544195592066, 'NEW_EDGE', '22', '5', '-16776961', 'CYCLE_LEFT'],
    [1544195592066, 'NEW_EDGE', 'MP', '23', '-65536', 'SUP_EDGE'],
    [1544195592066, 'NEW_EDGE', '22', '11', '-16776961', 'CYCLE_RIGHT'],
    [1544195592066, 'NEW_EDGE', '23', '11', '-16776961', 'CYCLE_LEFT'],
    [1544195592066, 'NEW_EDGE', '23', '1', '-16776961', 'CYCLE_RIGHT'],
    [1544195592068, 'NEW_EDGE', 'MP', '24', '-65536', 'SUP_EDGE'],
    [1544195592069, 'NEW_EDGE', '24', '1', '-16776961', 'CYCLE_LEFT'],
    [1544195592069, 'NEW_EDGE', '24', '12', '-16776961', 'CYCLE_RIGHT'],
    [1544195592069, 'REMOVE_EDGE', '11', '5', '-16776961', 'CYCLE_LEFT'],
    [1544195592069, 'NEW_EDGE', '11', '22', '-16776961', 'CYCLE_LEFT'],
    [1544195592069, 'NEW_EDGE', 'MP', '25', '-65536', 'SUP_EDGE'],
    [1544195592070, 'NEW_EDGE', '25', '12', '-16776961', 'CYCLE_LEFT'],
    [1544195592070, 'NEW_EDGE', '25', '6', '-16776961', 'CYCLE_RIGHT'],
    [1544195592071, 'REMOVE_EDGE', '12', '6', '-16776961', 'CYCLE_RIGHT'],
    [1544195592071, 'NEW_EDGE', '12', '25', '-16776961', 'CYCLE_RIGHT'],
    [1544195592071, 'NEW_EDGE', 'MP', '26', '-65536', 'SUP_EDGE'],
    [1544195592071, 'REMOVE_EDGE', '11', '1', '-16776961', 'CYCLE_RIGHT'],
    [1544195592071, 'NEW_EDGE', '11', '23', '-16776961', 'CYCLE_RIGHT'],
    [1544195592073, 'NEW_EDGE', '26', '6', '-16776961', 'CYCLE_LEFT'],
    [1544195592073, 'REMOVE_EDGE', '12', '1', '-16776961', 'CYCLE_LEFT'],
    [1544195592073, 'NEW_EDGE', '12', '24', '-16776961', 'CYCLE_LEFT'],
    [1544195592073, 'NEW_EDGE', '26', '13', '-16776961', 'CYCLE_RIGHT'],
    [1544195592073, 'NEW_EDGE', 'MP', '27', '-65536', 'SUP_EDGE'],
    [1544195592074, 'NEW_EDGE', '27', '13', '-16776961', 'CYCLE_LEFT'],
    [1544195592074, 'NEW_EDGE', '27', '3', '-16776961', 'CYCLE_RIGHT'],
    [1544195592075, 'NEW_EDGE', 'MP', '28', '-65536', 'SUP_EDGE'],
    [1544195592077, 'NEW_EDGE', '28', '3', '-16776961', 'CYCLE_LEFT'],
    [1544195592077, 'NEW_EDGE', '28', '14', '-16776961', 'CYCLE_RIGHT'],
    [1544195592077, 'REMOVE_EDGE', '13', '6', '-16776961', 'CYCLE_LEFT'],
    [1544195592077, 'NEW_EDGE', '13', '26', '-16776961', 'CYCLE_LEFT'],
    [1544195592078, 'NEW_EDGE', 'MP', '29', '-65536', 'SUP_EDGE'],
    [1544195592078, 'REMOVE_EDGE', '14', '3', '-16776961', 'CYCLE_LEFT'],
    [1544195592078, 'REMOVE_EDGE', '13', '3', '-16776961', 'CYCLE_RIGHT'],
    [1544195592078, 'NEW_EDGE', '13', '27', '-16776961', 'CYCLE_RIGHT'],
    [1544195592078, 'NEW_EDGE', '14', '28', '-16776961', 'CYCLE_LEFT'],
    [1544195592078, 'NEW_EDGE', '29', '14', '-16776961', 'CYCLE_LEFT'],
    [1544195592078, 'NEW_EDGE', '29', '7', '-16776961', 'CYCLE_RIGHT'],
    [1544195592080, 'REMOVE_EDGE', '14', '7', '-16776961', 'CYCLE_RIGHT'],
    [1544195592080, 'NEW_EDGE', '14', '29', '-16776961', 'CYCLE_RIGHT'],
    [1544195592080, 'NEW_EDGE', '0', '1', '-16777092', 'SHORTCUT'],
    [1544195592081, 'NEW_EDGE', 'MP', '30', '-65536', 'SUP_EDGE'],
    [1544195592082, 'NEW_EDGE', '30', '7', '-16776961', 'CYCLE_LEFT'],
    [1544195592082, 'NEW_EDGE', '30', '15', '-16776961', 'CYCLE_RIGHT'],
    [1544195592083, 'NEW_EDGE', 'MP', '31', '-65536', 'SUP_EDGE'],
    [1544195592085, 'NEW_EDGE', '31', '15', '-16776961', 'CYCLE_LEFT'],
    [1544195592085, 'NEW_EDGE', '31', '0', '-16776961', 'CYCLE_CYCLE'],
    [1544195592085, 'REMOVE_EDGE', '15', '7', '-16776961', 'CYCLE_LEFT'],
    [1544195592085, 'NEW_EDGE', '15', '30', '-16776961', 'CYCLE_LEFT'],
    [1544195592088, 'NEW_EDGE', '15', '31', '-16776961', 'CYCLE_RIGHT'],
    [1544195592089, 'REMOVE_EDGE', '15', '0', '-16776961', 'CYCLE_CYCLE'],
    [1544195592096, 'REMOVE_EDGE', '7', '3', '-16776961', 'CYCLE_LEFT'],
    [1544195592098, 'NEW_EDGE', '7', '0', '-16776961', 'CYCLE_LEFT'],
    [1544195592101, 'REMOVE_EDGE', '7', '0', '-16776961', 'CYCLE_LEFT'],
    [1544195592101, 'NEW_EDGE', '7', '3', '-16776961', 'CYCLE_LEFT'],
    [1544195592117, 'NEW_EDGE', '3', '1', '-16777092', 'SHORTCUT'],
    [1544195592170, 'NEW_EDGE', '0', '2', '-16777092', 'SHORTCUT'],
    [1544195592177, 'REMOVE_EDGE', '9', '4', '-16776961', 'CYCLE_LEFT'],
    [1544195592177, 'NEW_EDGE', '9', '18', '-16776961', 'CYCLE_LEFT'],
    [1544195592178, 'REMOVE_EDGE', '1', '5', '-16776961', 'CYCLE_LEFT'],
    [1544195592178, 'NEW_EDGE', '1', '11', '-16776961', 'CYCLE_LEFT'],
    [1544195592178, 'NEW_EDGE', '2', '1', '-16777092', 'SHORTCUT'],
    [1544195592182, 'REMOVE_EDGE', '0', '7', '-16776961', 'CYCLE_CYCLE'],
    [1544195592182, 'NEW_EDGE', '0', '15', '-16776961', 'CYCLE_CYCLE'],
    [1544195592187, 'REMOVE_EDGE', '1', '6', '-16776961', 'CYCLE_RIGHT'],
    [1544195592187, 'NEW_EDGE', '1', '12', '-16776961', 'CYCLE_RIGHT'],
    [1544195592187, 'NEW_EDGE', '4', '0', '-16777092', 'SHORTCUT'],
    [1544195592189, 'REMOVE_EDGE', '3', '6', '-16776961', 'CYCLE_LEFT'],
    [1544195592189, 'NEW_EDGE', '3', '13', '-16776961', 'CYCLE_LEFT'],
    [1544195592191, 'REMOVE_EDGE', '9', '2', '-16776961', 'CYCLE_RIGHT'],
    [1544195592191, 'NEW_EDGE', '9', '19', '-16776961', 'CYCLE_RIGHT'],
    [1544195592191, 'REMOVE_EDGE', '0', '8', '-16776961', 'CYCLE_RIGHT'],
    [1544195592191, 'NEW_EDGE', '0', '16', '-16776961', 'CYCLE_RIGHT'],
    [1544195592193, 'REMOVE_EDGE', '7', '3', '-16776961', 'CYCLE_LEFT'],
    [1544195592193, 'NEW_EDGE', '7', '14', '-16776961', 'CYCLE_LEFT'],
    [1544195592193, 'REMOVE_EDGE', '3', '7', '-16776961', 'CYCLE_RIGHT'],
    [1544195592193, 'NEW_EDGE', '3', '14', '-16776961', 'CYCLE_RIGHT'],
    [1544195592201, 'REMOVE_EDGE', '10', '2', '-16776961', 'CYCLE_LEFT'],
    [1544195592201, 'NEW_EDGE', '10', '20', '-16776961', 'CYCLE_LEFT'],
    [1544195592214, 'REMOVE_EDGE', '10', '5', '-16776961', 'CYCLE_RIGHT'],
    [1544195592214, 'NEW_EDGE', '10', '21', '-16776961', 'CYCLE_RIGHT'],
    [1544195592215, 'NEW_EDGE', '7', '15', '-16776961', 'CYCLE_RIGHT'],
    [1544195592216, 'REMOVE_EDGE', '7', '0', '-16776961', 'CYCLE_CYCLE'],
    [1544195592244, 'NEW_EDGE', '0', '3', '-16777092', 'SHORTCUT'],
    [1544195592256, 'REMOVE_EDGE', '4', '8', '-16776961', 'CYCLE_LEFT'],
    [1544195592256, 'REMOVE_EDGE', '1', '11', '-16776961', 'CYCLE_LEFT'],
    [1544195592256, 'NEW_EDGE', '1', '23', '-16776961', 'CYCLE_LEFT'],
    [1544195592256, 'NEW_EDGE', '4', '17', '-16776961', 'CYCLE_LEFT'],
    [1544195592262, 'REMOVE_EDGE', '4', '9', '-16776961', 'CYCLE_RIGHT'],
    [1544195592262, 'NEW_EDGE', '4', '18', '-16776961', 'CYCLE_RIGHT'],
    [1544195592268, 'REMOVE_EDGE', '1', '12', '-16776961', 'CYCLE_RIGHT'],
    [1544195592268, 'NEW_EDGE', '1', '24', '-16776961', 'CYCLE_RIGHT'],
    [1544195592279, 'REMOVE_EDGE', '2', '9', '-16776961', 'CYCLE_LEFT'],
    [1544195592279, 'NEW_EDGE', '2', '19', '-16776961', 'CYCLE_LEFT'],
    [1544195592280, 'REMOVE_EDGE', '5', '1', '-16776961', 'CYCLE_RIGHT'],
    [1544195592280, 'NEW_EDGE', '5', '11', '-16776961', 'CYCLE_RIGHT'],
    [1544195592290, 'REMOVE_EDGE', '6', '1', '-16776961', 'CYCLE_LEFT'],
    [1544195592290, 'NEW_EDGE', '6', '12', '-16776961', 'CYCLE_LEFT'],
    [1544195592290, 'REMOVE_EDGE', '2', '10', '-16776961', 'CYCLE_RIGHT'],
    [1544195592290, 'NEW_EDGE', '2', '20', '-16776961', 'CYCLE_RIGHT'],
    [1544195592304, 'REMOVE_EDGE', '8', '0', '-16776961', 'CYCLE_LEFT'],
    [1544195592304, 'NEW_EDGE', '8', '16', '-16776961', 'CYCLE_LEFT'],
    [1544195592307, 'REMOVE_EDGE', '3', '13', '-16776961', 'CYCLE_LEFT'],
    [1544195592307, 'NEW_EDGE', '3', '27', '-16776961', 'CYCLE_LEFT'],
    [1544195592308, 'REMOVE_EDGE', '6', '3', '-16776961', 'CYCLE_RIGHT'],
    [1544195592308, 'NEW_EDGE', '6', '13', '-16776961', 'CYCLE_RIGHT'],
    [1544195592318, 'REMOVE_EDGE', '8', '4', '-16776961', 'CYCLE_RIGHT'],
    [1544195592318, 'NEW_EDGE', '8', '17', '-16776961', 'CYCLE_RIGHT'],
    [1544195592319, 'REMOVE_EDGE', '3', '14', '-16776961', 'CYCLE_RIGHT'],
    [1544195592319, 'NEW_EDGE', '3', '28', '-16776961', 'CYCLE_RIGHT'],
    [1544195592320, 'REMOVE_EDGE', '0', '15', '-16776961', 'CYCLE_CYCLE'],
    [1544195592320, 'NEW_EDGE', '0', '31', '-16776961', 'CYCLE_CYCLE'],
    [1544195592324, 'REMOVE_EDGE', '0', '31', '-16776961', 'CYCLE_CYCLE'],
    [1544195592325, 'NEW_EDGE', '0', '15', '-16776961', 'CYCLE_CYCLE'],
    [1544195592331, 'REMOVE_EDGE', '5', '10', '-16776961', 'CYCLE_LEFT'],
    [1544195592331, 'NEW_EDGE', '5', '21', '-16776961', 'CYCLE_LEFT'],
    [1544195592331, 'REMOVE_EDGE', '31', '15', '-16776961', 'CYCLE_LEFT'],
    [1544195592333, 'NEW_EDGE', '31', '15', '-16776961', 'CYCLE_LEFT'],
    [1544195592335, 'REMOVE_EDGE', '0', '15', '-16776961', 'CYCLE_CYCLE'],
    [1544195592335, 'NEW_EDGE', '0', '31', '-16776961', 'CYCLE_CYCLE'],
    [1544195592342, 'REMOVE_EDGE', '5', '11', '-16776961', 'CYCLE_RIGHT'],
    [1544195592342, 'NEW_EDGE', '5', '22', '-16776961', 'CYCLE_RIGHT'],
    [1544195592349, 'REMOVE_EDGE', '7', '14', '-16776961', 'CYCLE_LEFT'],
    [1544195592349, 'NEW_EDGE', '7', '29', '-16776961', 'CYCLE_LEFT'],
    [1544195592353, 'REMOVE_EDGE', '31', '0', '-16776961', 'CYCLE_CYCLE'],
    [1544195592357, 'NEW_EDGE', '31', '0', '-16776961', 'CYCLE_CYCLE'],
    [1544195592360, 'REMOVE_EDGE', '31', '15', '-16776961', 'CYCLE_LEFT'],
    [1544195592366, 'REMOVE_EDGE', '7', '15', '-16776961', 'CYCLE_RIGHT'],
    [1544195592366, 'NEW_EDGE', '7', '30', '-16776961', 'CYCLE_RIGHT'],
    [1544195592368, 'NEW_EDGE', '31', '7', '-16776961', 'CYCLE_LEFT'],
    [1544195592370, 'REMOVE_EDGE', '31', '7', '-16776961', 'CYCLE_LEFT'],
    [1544195592370, 'NEW_EDGE', '31', '15', '-16776961', 'CYCLE_LEFT'],
    [1544195592452, 'REMOVE_EDGE', '6', '12', '-16776961', 'CYCLE_LEFT'],
    [1544195592452, 'NEW_EDGE', '6', '25', '-16776961', 'CYCLE_LEFT'],
    [1544195592454, 'NEW_EDGE', '1', '3', '-16777092', 'SHORTCUT'],
    [1544195592455, 'REMOVE_EDGE', '31', '15', '-16776961', 'CYCLE_LEFT'],
    [1544195592457, 'NEW_EDGE', '31', '0', '-16776961', 'CYCLE_LEFT'],
    [1544195592459, 'REMOVE_EDGE', '31', '0', '-16776961', 'CYCLE_LEFT'],
    [1544195592459, 'NEW_EDGE', '31', '15', '-16776961', 'CYCLE_LEFT'],
    [1544195592477, 'REMOVE_EDGE', '6', '13', '-16776961', 'CYCLE_RIGHT'],
    [1544195592477, 'NEW_EDGE', '6', '26', '-16776961', 'CYCLE_RIGHT'],
    [1544195592480, 'REMOVE_EDGE', '31', '0', '-16776961', 'CYCLE_CYCLE'],
    [1544195592491, 'NEW_EDGE', '31', '0', '-16776961', 'CYCLE_CYCLE'],
    [1544195592518, 'NEW_EDGE', '2', '0', '-16777092', 'SHORTCUT'],
    [1544195592614, 'NEW_EDGE', '2', '4', '-16777092', 'SHORTCUT'],
    [1544195592673, 'REMOVE_EDGE', '31', '0', '-16776961', 'CYCLE_CYCLE'],
    [1544195592679, 'NEW_EDGE', '31', '0', '-16776961', 'CYCLE_CYCLE'],
    [1544195592758, 'NEW_EDGE', '5', '2', '-16777092', 'SHORTCUT'],
    [1544195592775, 'REMOVE_EDGE', '31', '15', '-16776961', 'CYCLE_LEFT'],
    [1544195592777, 'NEW_EDGE', '31', '0', '-16776961', 'CYCLE_LEFT'],
    [1544195592779, 'REMOVE_EDGE', '31', '0', '-16776961', 'CYCLE_LEFT'],
    [1544195592779, 'NEW_EDGE', '31', '15', '-16776961', 'CYCLE_LEFT'],
    [1544195592797, 'NEW_EDGE', '4', '2', '-16777092', 'SHORTCUT'],
    [1544195592847, 'NEW_EDGE', '11', '1', '-16777092', 'SHORTCUT'],
    [1544195592925, 'NEW_EDGE', '13', '3', '-16777092', 'SHORTCUT'],
    [1544195592938, 'NEW_EDGE', '12', '1', '-16777092', 'SHORTCUT'],
    [1544195593006, 'REMOVE_EDGE', '15', '31', '-16776961', 'CYCLE_RIGHT'],
    [1544195593008, 'NEW_EDGE', '15', '31', '-16776961', 'CYCLE_RIGHT'],
    [1544195593013, 'REMOVE_EDGE', '30', '15', '-16776961', 'CYCLE_RIGHT'],
    [1544195593013, 'NEW_EDGE', '0', '4', '-16777092', 'SHORTCUT'],
    [1544195593018, 'NEW_EDGE', '30', '15', '-16776961', 'CYCLE_RIGHT'],
    [1544195593102, 'NEW_EDGE', '11', '5', '-16777092', 'SHORTCUT'],
    [1544195593139, 'REMOVE_EDGE', '30', '15', '-16776961', 'CYCLE_RIGHT'],
    [1544195593144, 'NEW_EDGE', '30', '15', '-16776961', 'CYCLE_RIGHT'],
    [1544195593219, 'NEW_EDGE', '3', '0', '-16777092', 'SHORTCUT'],
    [1544195593258, 'NEW_EDGE', '15', '7', '-16777092', 'SHORTCUT'],
    [1544195593340, 'NEW_EDGE', '14', '3', '-16777092', 'SHORTCUT'],
    [1544195593408, 'NEW_EDGE', '14', '7', '-16777092', 'SHORTCUT'],
    [1544195593452, 'NEW_EDGE', '2', '5', '-16777092', 'SHORTCUT'],
    [1544195593464, 'REMOVE_EDGE', '31', '15', '-16776961', 'CYCLE_LEFT'],
    [1544195593465, 'NEW_EDGE', '31', '0', '-16776961', 'CYCLE_LEFT'],
    [1544195593467, 'REMOVE_EDGE', '31', '0', '-16776961', 'CYCLE_LEFT'],
    [1544195593467, 'NEW_EDGE', '31', '15', '-16776961', 'CYCLE_LEFT'],
    [1544195593510, 'NEW_EDGE', '13', '6', '-16777092', 'SHORTCUT'],
    [1544195593528, 'REMOVE_EDGE', '30', '15', '-16776961', 'CYCLE_RIGHT'],
    [1544195593533, 'NEW_EDGE', '30', '15', '-16776961', 'CYCLE_RIGHT'],
    [1544195593609, 'NEW_EDGE', '12', '6', '-16777092', 'SHORTCUT'],
    [1544195593758, 'REMOVE_EDGE', '31', '0', '-16776961', 'CYCLE_CYCLE'],
    [1544195593761, 'NEW_EDGE', '31', '0', '-16776961', 'CYCLE_CYCLE'],
    [1544195593944, 'REMOVE_EDGE', '30', '15', '-16776961', 'CYCLE_RIGHT'],
    [1544195593949, 'NEW_EDGE', '30', '15', '-16776961', 'CYCLE_RIGHT'],
    [1544195594191, 'REMOVE_EDGE', '15', '31', '-16776961', 'CYCLE_RIGHT'],
    [1544195594194, 'NEW_EDGE', '15', '31', '-16776961', 'CYCLE_RIGHT'],
    [1544195594309, 'REMOVE_EDGE', '30', '15', '-16776961', 'CYCLE_RIGHT'],
    [1544195594315, 'NEW_EDGE', '30', '15', '-16776961', 'CYCLE_RIGHT'],
    [1544195594779, 'REMOVE_EDGE', '30', '15', '-16776961', 'CYCLE_RIGHT'],
    [1544195594783, 'NEW_EDGE', '30', '15', '-16776961', 'CYCLE_RIGHT'],
    [1544195594946, 'NEW_EDGE', '9', '4', '-16777092', 'SHORTCUT'],
    [1544195595046, 'REMOVE_EDGE', '30', '15', '-16776961', 'CYCLE_RIGHT'],
    [1544195595051, 'NEW_EDGE', '30', '15', '-16776961', 'CYCLE_RIGHT'],
    [1544195595134, 'NEW_EDGE', '9', '2', '-16777092', 'SHORTCUT'],
    [1544195595224, 'NEW_EDGE', '10', '2', '-16777092', 'SHORTCUT'],
    [1544195595465, 'REMOVE_EDGE', '30', '15', '-16776961', 'CYCLE_RIGHT'],
    [1544195595470, 'NEW_EDGE', '30', '15', '-16776961', 'CYCLE_RIGHT'],
    [1544195595581, 'NEW_EDGE', '10', '5', '-16777092', 'SHORTCUT'],
    [1544195595799, 'NEW_EDGE', '0', '15', '-16777092', 'SHORTCUT'],
    [1544195596319, 'NEW_EDGE', '0', '8', '-16777092', 'SHORTCUT'],
    [1544195596433, 'NEW_EDGE', '4', '8', '-16777092', 'SHORTCUT'],
    [1544195596446, 'REMOVE_EDGE', '31', '0', '-16776961', 'CYCLE_CYCLE'],
    [1544195596448, 'NEW_EDGE', '31', '0', '-16776961', 'CYCLE_CYCLE'],
    [1544195596658, 'NEW_EDGE', '8', '4', '-16777092', 'SHORTCUT'],
    [1544195596983, 'NEW_EDGE', '5', '11', '-16777092', 'SHORTCUT'],
    [1544195597542, 'REMOVE_EDGE', '31', '15', '-16776961', 'CYCLE_LEFT'],
    [1544195597543, 'NEW_EDGE', '31', '0', '-16776961', 'CYCLE_LEFT'],
    [1544195597544, 'REMOVE_EDGE', '31', '0', '-16776961', 'CYCLE_LEFT'],
    [1544195597544, 'NEW_EDGE', '31', '15', '-16776961', 'CYCLE_LEFT'],
    [1544195598042, 'NEW_EDGE', '8', '0', '-16777092', 'SHORTCUT'],
    [1544195598126, 'NEW_EDGE', '4', '9', '-16777092', 'SHORTCUT'],
    [1544195598176, 'NEW_EDGE', '1', '11', '-16777092', 'SHORTCUT'],
    [1544195598290, 'NEW_EDGE', '3', '13', '-16777092', 'SHORTCUT'],
    [1544195598456, 'NEW_EDGE', '2', '9', '-16777092', 'SHORTCUT'],
    [1544195598859, 'NEW_EDGE', '2', '10', '-16777092', 'SHORTCUT'],
    [1544195599085, 'NEW_EDGE', '3', '14', '-16777092', 'SHORTCUT'],
    [1544195599103, 'NEW_EDGE', '5', '10', '-16777092', 'SHORTCUT'],
    [1544195599430, 'NEW_EDGE', '1', '12', '-16777092', 'SHORTCUT'],
    [1544195599852, 'NEW_EDGE', '7', '14', '-16777092', 'SHORTCUT'],
    [1544195600295, 'NEW_EDGE', '6', '13', '-16777092', 'SHORTCUT'],
    [1544195600325, 'NEW_EDGE', '6', '12', '-16777092', 'SHORTCUT'],
    [1544195600726, 'NEW_EDGE', '1', '5', '-16777092', 'SHORTCUT'],
    [1544195601232, 'NEW_EDGE', '3', '7', '-16777092', 'SHORTCUT'],
    [1544195601258, 'NEW_EDGE', '3', '6', '-16777092', 'SHORTCUT'],
    [1544195601828, 'NEW_EDGE', '0', '7', '-16777092', 'SHORTCUT'],
    [1544195602078, 'NEW_EDGE', '7', '15', '-16777092', 'SHORTCUT'],
    [1544195602354, 'NEW_EDGE', '7', '3', '-16777092', 'SHORTCUT'],
    [1544195602639, 'NEW_EDGE', '1', '6', '-16777092', 'SHORTCUT'],
    [1544195603450, 'NEW_EDGE', '6', '3', '-16777092', 'SHORTCUT'],
    [1544195604743, 'NEW_EDGE', '5', '1', '-16777092', 'SHORTCUT'],
    [1544195606259, 'REMOVE_EDGE', '31', '15', '-16776961', 'CYCLE_LEFT'],
    [1544195606260, 'NEW_EDGE', '31', '0', '-16776961', 'CYCLE_LEFT'],
    [1544195606262, 'REMOVE_EDGE', '31', '0', '-16776961', 'CYCLE_LEFT'],
    [1544195606262, 'NEW_EDGE', '31', '15', '-16776961', 'CYCLE_LEFT'],
    [1544195607136, 'NEW_EDGE', '6', '1', '-16777092', 'SHORTCUT'],
    [1544195608482, 'REMOVE_EDGE', '31', '0', '-16776961', 'CYCLE_CYCLE'],
    [1544195608483, 'NEW_EDGE', '31', '0', '-16776961', 'CYCLE_CYCLE'],

  ];

  constructor() { }


  ngAfterViewInit() {
    this.graph.graph.simulation.force('collide', d3.forceCollide().radius(50));

    // this.graph.graph.simulation.force('charge', d3.forceManyBody().strength(-20).distanceMax(200).distanceMin(200))
    this.graph.graph.simulation.force('charge', null);
    this.graph.graph.simulation.force('x', null);
    this.graph.graph.simulation.force('y', null);
    this.graph.graph.simulation.force('links', null);

    // this.graph.graph.simulation.force('centers', null);
    // this.graph.graph.simulation.force('radial', d3.forceRadial(200, 400,400));

    this.replay();
  }
  getNode = (id: string) => {

    if (this.graph.graph.nodes.filter(x => x.id === id).length === 0) {
      const node = new Node(id, id);
      node.x = Math.random() * 100;
      node.y = Math.random() * 100;
      this.graph.graph.nodes.push(node);
      return node;
    } else {
      return this.graph.graph.nodes.filter(x => x.id === id).shift();
    }

  }

  async replay() {

    const market_provider = new Node('MP', 'MP');
    market_provider.x = Math.random() * 100;
    market_provider.y = Math.random() * 100;
    this.graph.graph.nodes.push(market_provider);

    for (let i = 0; i < 32; i++) {
      this.getNode(this.toStr(i));
    }
    this.restart();

    for (const row of this.log) {
      const rowS = row.map(x => this.toStr(x));

      if (row instanceof Array) {
        // const timestamp: number = row[0];
        const eventType = rowS[1];
        const source_id = rowS[2];
        const target_id = rowS[3];
        const edge_type = rowS[5];
        const domainHash = row[4]; // it is the color;

        switch (eventType) {
          case 'NEW_EDGE':
            const source_node = this.getNode(source_id);
            const target_node = this.getNode(target_id);
            const link = new TypedLink(source_node, target_node, edge_type);
            this.linksMap[`${source_id}_${target_id}_${edge_type}`] = link;
            this.graph.graph.links.push(link);
            // source_node.linkCount += 0.1;
            // target_node.linkCount += 0.1;
            this.restart();
            break;

          case 'REMOVE_EDGE':
            if (this.linksMap[`${source_id}_${target_id}_${edge_type}`] !== undefined) {
              const index = this.links.indexOf(this.linksMap[`${source_id}_${target_id}_${edge_type}`]);
              this.links.splice(index, 1);
            } else {
              console.log(`edge ${source_id}_${target_id}_${edge_type} does not exist.`);
            }
            break;
        }

        await this.sleep(50);
      }
    }

  }

  restart() {
    this.graph.graph.simulation.nodes(this.graph.graph.nodes);
    this.graph.graph.simulation.force('link', d3.forceLink(this.graph.graph.links).distance(200));
    this.graph.graph.simulation.alpha(1).restart();
  }

  sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

  toStr(val: any): string {
    if (val !== undefined) {
      return val.toString();
    }
  }

}

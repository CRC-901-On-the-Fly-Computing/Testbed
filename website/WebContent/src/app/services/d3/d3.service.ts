import { Injectable, EventEmitter } from '@angular/core';
import { Node, Link, ForceDirectedGraph } from './models';
import { event as d3Event } from 'd3-selection';
import { zoom as d3Zoom } from 'd3-zoom';
import { drag as d3Drag } from 'd3-drag';
import { select as d3Select } from 'd3-selection';

@Injectable()
export class D3Service {
  /** This service will provide methods to enable user interaction with elements
    * while maintaining the d3 simulations physics
    */
  constructor() { }

  /** A method to bind a pan and zoom behaviour to an svg element */
  applyZoomableBehaviour(svgElement, containerElement) {
    let svg, container, zoomed, zoom;

    svg = d3Select(svgElement);
    container = d3Select(containerElement);

    zoomed = () => {
      const transform = d3Event.transform;
      const scale = Number(containerElement.getAttribute('transform').split('scale(')[1].replace(')', ''));
      // container.attr('transform', 'translate(' + transform.x + ',' + transform.y + ') scale(' + transform.k + ')');
      container.attr('transform', 'translate(' + transform.x + ',' + transform.y + ') scale(' + scale + ')');

    };

    zoom = d3Zoom().on('zoom', zoomed);
    svg.call(zoom);
    // Disabled the scroll and just allow the graph to move not scaling
    svg.call(zoom).on('wheel.zoom', null);
  }

  /** A method to bind a draggable behaviour to an svg element */
  applyDraggableBehaviour(element, node: Node, graph: ForceDirectedGraph) {
    const d3element = d3Select(element);

    function started() {
      /** Preventing propagation of dragstart to parent elements */
      d3Event.sourceEvent.stopPropagation();

      if (!d3Event.active) {
        graph.simulation.alphaTarget(0.3).restart();
      }

      d3Event.on('drag', dragged).on('end', ended);

      function dragged() {
        node.fx = d3Event.x;
        node.fy = d3Event.y;
      }

      function ended() {
        if (!d3Event.active) {
          graph.simulation.alphaTarget(0);
        }

        node.fx = null;
        node.fy = null;
      }
    }

    d3element.call(d3Drag()
      .on('start', started));

    try {
      d3Event.sourceEvent.stopPropagation();
    } catch (err) {

    }
  }

  /** The interactable graph we will simulate in this article
  * This method does not interact with the document, purely physical calculations with d3
  */
  getForceDirectedGraph(nodes: Node[], links: Link[], options: { width, height }) {
    const sg = new ForceDirectedGraph(nodes, links, options);
    return sg;
  }

  select(el) {
    return d3Select(el);
  }

}

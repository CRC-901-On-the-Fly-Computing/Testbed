import APP_CONFIG from '../../../app.config';

export class Node implements d3.SimulationNodeDatum {
  // optional - defining optional implementation properties - required for relevant typing assistance
  index?: number;
  x?: number;
  y?: number;
  vx?: number;
  vy?: number;
  fx?: number | null;
  fy?: number | null;

  id: string;
  label: string;
  configurationLabel: string;
  linkCount = 0;

  constructor(id, label?: string, configurationLabel ?: string) {
    this.id = id;
    this.label = label;
    this.configurationLabel = configurationLabel;
  }

  normal = () => {
    return Math.sqrt(this.linkCount / APP_CONFIG.N);
  }

  get r() {
    return 50 * this.normal() + 12.5;
  }

  get fontSize() {
    return (30 * this.normal() + 10) + 'px';
  }

  get color() {
    const index = Math.floor(APP_CONFIG.SPECTRUM.length * this.normal());
    const random_color = APP_CONFIG.SPECTRUM[index];

    return APP_CONFIG.config_colors[this.label] || random_color;
  }
}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class ExecutorsService {
  constructor(private http: HttpClient) { }

  getServices(baseUrl: string) {
    return this.get(baseUrl, 'services');
  }

  getAddresses(baseUrl: string) {
    return this.get(baseUrl, 'addresses');
  }

  get(baseUrl: string, url: string): Observable<any> {
    return this.http.get(`${baseUrl}${url}`, { responseType: 'text' });
  }

  getLog(url: string, uuid: string): Observable<any> {
    if (url.indexOf('http') === -1) {
      url = 'http://' + url;
    }
    return this.http.get(`${url}/cmd/${uuid}/cat/logs/sede-1.log`, { responseType: 'text' });
  }

  parseCompositions(response: string) {
    const _res = {};
    let index = '';
    response.replace('Registered executors:\n', '').split('\n').forEach(x => {
      if (x.indexOf('		') !== 0 && x.indexOf('	') === 0) {
        index = x.trim();
        _res[index] = [];
      } else {
        if (x.indexOf('		') === 0) {
          _res[index].push(x.trim());
        }
      }
    });
    return _res;
  }

  parseUrls(response: string) {
    const _res = [];
    response.replace('Registered executors: \n', '').split('\n').forEach(x => {
      const values = x.split(' ').map(y => y.trim());
      if (values && values[0] && values[1]) {
        _res.push({
          uuid: values[0],
          url: values[1]
        });
      }
    });
    return _res;
  }

}

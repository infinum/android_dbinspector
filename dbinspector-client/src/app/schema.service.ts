import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Schema} from "./schema";

@Injectable({
  providedIn: 'root'
})
export class SchemaService {

  private schemaUrl = '/api/v1/databases';

  constructor(
    private http: HttpClient
  ) {
  }

  tablesById(id: string): Observable<Schema> {
    return this.http.get<Schema>(`${this.schemaUrl}/${id}/tables`)
  }

  viewsById(id: string): Observable<Schema> {
    return this.http.get<Schema>(`${this.schemaUrl}/${id}/views`)
  }

  triggersById(id: string): Observable<Schema> {
    return this.http.get<Schema>(`${this.schemaUrl}/${id}/triggers`)
  }
}

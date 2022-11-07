import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Schema} from "./schema";
import {environment} from "../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class SchemaService {

  private apiUrl = `${environment.apiPrefix}/api/v1/databases`;

  constructor(
    private http: HttpClient
  ) {
  }

  tablesById(id: string): Observable<Schema> {
    return this.http.get<Schema>(`${this.apiUrl}/${id}/tables`)
  }

  viewsById(id: string): Observable<Schema> {
    return this.http.get<Schema>(`${this.apiUrl}/${id}/views`)
  }

  triggersById(id: string): Observable<Schema> {
    return this.http.get<Schema>(`${this.apiUrl}/${id}/triggers`)
  }
}

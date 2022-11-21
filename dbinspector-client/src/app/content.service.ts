import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Page} from "./page";

@Injectable({
  providedIn: 'root'
})
export class ContentService {

  private apiUrl = `/api/v1/databases`;

  constructor(
    private http: HttpClient
  ) {
  }

  tableById(databaseId: string, schemaId: string): Observable<Page> {
    return this.http.get<Page>(`${this.apiUrl}/${databaseId}/tables/${schemaId}`)
  }

  viewById(databaseId: string, schemaId: string): Observable<Page> {
    return this.http.get<Page>(`${this.apiUrl}/${databaseId}/views/${schemaId}`)
  }

  triggerById(databaseId: string, schemaId: string): Observable<Page> {
    return this.http.get<Page>(`${this.apiUrl}/${databaseId}/triggers/${schemaId}`)
  }

  clearTableById(databaseId: string, schemaId: string) {
    return this.http.delete(`${this.apiUrl}/${databaseId}/tables/${schemaId}`)
  }

  dropViewById(databaseId: string, schemaId: string) {
    return this.http.delete(`${this.apiUrl}/${databaseId}/views/${schemaId}`)
  }

  dropTriggerById(databaseId: string, schemaId: string) {
    return this.http.delete(`${this.apiUrl}/${databaseId}/triggers/${schemaId}`)
  }
}

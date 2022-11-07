import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Schema} from "./schema";
import {Page} from "./page";
import {environment} from "../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class ContentService {

  private apiUrl = `${environment.apiPrefix}/api/v1/databases`;

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
}

import {Injectable} from '@angular/core';
import {environment} from "../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Page} from "./page";

@Injectable({
  providedIn: 'root'
})
export class PragmaService {

  private apiUrl = `${environment.apiPrefix}/api/v1/databases`;

  constructor(
    private http: HttpClient
  ) {
  }

  tableInfoById(databaseId: string, schemaType: string, schemaId: string): Observable<Page> {
    return this.http.get<Page>(`${this.apiUrl}/${databaseId}/${schemaType}/${schemaId}/pragma/info`)
  }

  foreignKeysById(databaseId: string, schemaType: string, schemaId: string): Observable<Page> {
    return this.http.get<Page>(`${this.apiUrl}/${databaseId}/${schemaType}/${schemaId}/pragma/foreign_keys`)
  }

  indexesById(databaseId: string, schemaType: string, schemaId: string): Observable<Page> {
    return this.http.get<Page>(`${this.apiUrl}/${databaseId}/${schemaType}/${schemaId}/pragma/indexes`)
  }
}

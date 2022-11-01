import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Database} from "./database";

@Injectable({
  providedIn: 'root'
})
export class DatabaseService {

  private databasesUrl = '/api/v1/databases';

  constructor(
    private http: HttpClient
  ) {
  }

  databases(): Observable<Database[]> {
    return this.http.get<Database[]>(this.databasesUrl)
  }

}

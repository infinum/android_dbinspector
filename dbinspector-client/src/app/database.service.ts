import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, of} from 'rxjs';
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

  fetchAll(): Observable<Database[]> {
    return this.http.get<Database[]>(this.databasesUrl)
  }

  deleteById(id: string) {
    return this.http.delete(`${this.databasesUrl}/${id}`)
  }

  copyById(id: string) {
    const formData = new FormData();
    formData.set("id", id)
    return this.http.post(this.databasesUrl, formData)
  }

  downloadById(id: string) {
    return this.http.get(`${this.databasesUrl}/${id}`, { responseType: 'blob'})
  }
}

import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Database} from "./database";

@Injectable({
  providedIn: 'root'
})
export class DatabaseService {

  private apiUrl = `/api/v1/databases`;

  constructor(
    private http: HttpClient
  ) {
  }

  fetchAll(): Observable<Database[]> {
    return this.http.get<Database[]>(this.apiUrl)
  }

  deleteById(id: string) {
    return this.http.delete(`${this.apiUrl}/${id}`)
  }

  renameById(id: string, name: string) {
    const formData = new FormData();
    formData.set("name", name)
    return this.http.patch(`${this.apiUrl}/${id}`, formData)
  }

  copyById(id: string) {
    const formData = new FormData();
    formData.set("id", id)
    return this.http.post(this.apiUrl, formData)
  }

  downloadById(id: string) {
    return this.http.get(`${this.apiUrl}/${id}`, {responseType: 'blob'})
  }
}

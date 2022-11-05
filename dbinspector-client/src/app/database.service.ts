import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {NEVER, Observable, of, tap} from 'rxjs';
import {Database} from "./database";

@Injectable({
  providedIn: 'root'
})
export class DatabaseService {

  private cachedDatabases: Database[] = []

  private databasesUrl = '/api/v1/databases';

  constructor(
    private http: HttpClient
  ) {
  }

  fetchAll(): Observable<Database[]> {
    return this.http.get<Database[]>(this.databasesUrl)
      .pipe(
        tap(databases => this.cachedDatabases = databases)
      )
  }

  deleteById(id: string) {
    return this.http.delete(`${this.databasesUrl}/${id}`)
  }

  renameById(id: string, name: string) {
    const formData = new FormData();
    formData.set("name", name)
    return this.http.patch(`${this.databasesUrl}/${id}`, formData)
  }

  copyById(id: string) {
    const formData = new FormData();
    formData.set("id", id)
    return this.http.post(this.databasesUrl, formData)
  }

  downloadById(id: string) {
    return this.http.get(`${this.databasesUrl}/${id}`, {responseType: 'blob'})
  }

  fromCacheById(id: string) {
    const database = this.cachedDatabases.find(value => value.id == id)
    if (database != undefined) {
      return of(database)
    } else {
      return NEVER
    }
  }
}

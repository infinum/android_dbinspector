import {Injectable} from '@angular/core';
import {Database} from "./database";
import {SchemaCell} from "./schema_cell";

@Injectable({
  providedIn: 'root'
})
export class CacheService {

  public currentDatabase: Database | null = null
  public currentSchema: SchemaCell | null = null

  constructor() {
  }
}

import {SchemaCell} from "./schema_cell";

export interface Schema {
  nextPage: number | null,
  beforeCount: number,
  afterCount: number,
  cells: SchemaCell[]
}

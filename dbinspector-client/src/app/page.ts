export interface Page {
  nextPage: number | null,
  beforeCount: number,
  afterCount: number,
  columns: string[],
  rows: Row[]
}

export interface Row {
  cells: string[]
}

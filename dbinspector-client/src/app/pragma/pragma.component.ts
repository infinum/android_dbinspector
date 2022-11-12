import { Component, OnInit } from '@angular/core';
import {Location} from "@angular/common";
import {CacheService} from "../cache.service";
import {ActivatedRoute} from "@angular/router";
import {PragmaService} from "../pragma.service";
import {Row} from "../page";

@Component({
  selector: 'app-pragma',
  templateUrl: './pragma.component.html',
  styleUrls: ['./pragma.component.css']
})
export class PragmaComponent implements OnInit {

  databaseName?: string;
  schemaName?: string;

  tableInfoPageColumns: string[] = []
  tableInfoPageRows: Row[] = [];

  foreignKeysPageColumns: string[] = []
  foreignKeysPageRows: Row[] = [];

  indexesPageColumns: string[] = []
  indexesPageRows: Row[] = [];

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private pragmaService: PragmaService,
    private cacheService: CacheService
  ) { }

  ngOnInit(): void {
    this.fetchAll()
  }

  back(): void {
    this.location.back();
  }

  private fetchAll() {
    const databaseName = this.cacheService.currentDatabase?.name
    if (databaseName != null) {
      this.databaseName = databaseName
    }
    const schemaName = this.cacheService.currentSchema?.name
    if (schemaName != null) {
      this.schemaName = schemaName
    }
    const databaseId = String(this.route.snapshot.paramMap.get('database_id'));
    const schemaType = String(this.route.snapshot.paramMap.get('schema_type'));
    const schemaId = String(this.route.snapshot.paramMap.get('schema_id'));
    if (databaseId != null && schemaType != null && schemaId != null) {
      this.pragmaService
        .tableInfoById(databaseId, schemaType, schemaId)
        .subscribe(page => {
            if (page?.columns != null) {
              this.tableInfoPageColumns = page.columns;
            }
            if (page?.rows != null) {
              this.tableInfoPageRows = page.rows
            }
          }
        )
      this.pragmaService
        .foreignKeysById(databaseId, schemaType, schemaId)
        .subscribe(page => {
            if (page?.columns != null) {
              this.foreignKeysPageColumns = page.columns;
            }
            if (page?.rows != null) {
              this.foreignKeysPageRows = page.rows
            }
          }
        )
      this.pragmaService
        .indexesById(databaseId, schemaType, schemaId)
        .subscribe(page => {
            if (page?.columns != null) {
              this.indexesPageColumns = page.columns;
            }
            if (page?.rows != null) {
              this.indexesPageRows = page.rows
            }
          }
        )
    }
  }
}

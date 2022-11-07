import { Component, OnInit } from '@angular/core';
import {Row} from "../page";
import {ActivatedRoute, Router} from "@angular/router";
import {Location} from "@angular/common";
import {CacheService} from "../cache.service";
import {ContentService} from "../content.service";

@Component({
  selector: 'app-view',
  templateUrl: './view.component.html',
  styleUrls: ['./view.component.css']
})
export class ViewComponent implements OnInit {

  databaseName?: string;
  schemaName?: string;

  pageColumns: string[] = []
  pageRows: Row[] = [];

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private location: Location,
    private cacheService: CacheService,
    private contentService: ContentService
  ) {
  }

  ngOnInit(): void {
    this.fetchAll()
  }

  back(): void {
    this.cacheService.currentSchema = null
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
    const schemaId = String(this.route.snapshot.paramMap.get('schema_id'));
    if (databaseId != null && schemaId != null) {
      this.contentService
        .viewById(databaseId, schemaId)
        .subscribe(page => {
            if (page?.columns != null) {
              this.pageColumns = page.columns;
            }
            if (page?.rows != null) {
              this.pageRows = page.rows
            }
          }
        )
    }
  }
}

import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Location} from "@angular/common";
import {CacheService} from "../cache.service";
import {ContentService} from "../content.service";
import {Row} from "../page";
import {DeleteDatabaseComponent} from "../delete-database/delete-database.component";
import {ClearTableComponent} from "../clear-table/clear-table.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.css']
})
export class TableComponent implements OnInit {

  databaseName?: string;
  schemaName?: string;

  pageColumns: string[] = []
  pageRows: Row[] = [];

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private location: Location,
    private cacheService: CacheService,
    private contentService: ContentService,
    private dialog: MatDialog
  ) {
  }

  ngOnInit(): void {
    this.fetchAll()
  }

  back(): void {
    this.cacheService.currentSchema = null
    this.location.back();
  }

  confirmClear(): void {
    const databaseId = this.cacheService.currentDatabase?.id
    const schemaId = this.cacheService.currentSchema?.id
    const schemaName = this.cacheService.currentSchema?.name
    if (databaseId != null && schemaId != null) {
      const dialogRef = this.dialog.open(ClearTableComponent, {data: {name: schemaName}});
      dialogRef.afterClosed().subscribe(confirmed => {
        if (confirmed) {
          this.clearTable(databaseId, schemaId)
        }
      });
    }
  }

  showPragma(): void {
    const databaseId = this.cacheService.currentDatabase?.id
    const schemaId = this.cacheService.currentSchema?.id
    if (databaseId != null && schemaId != null) {
      this.router.navigateByUrl(`databases/${databaseId}/tables/${schemaId}/pragma`)
    }
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
        .tableById(databaseId, schemaId)
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

  private clearTable(databaseId: string, schemaId: string) {
    this.contentService
      .clearTableById(databaseId, schemaId)
      .subscribe(_ => this.fetchAll())
  }
}

import {Component, OnInit} from '@angular/core';
import {Row} from "../page";
import {ActivatedRoute, Router} from "@angular/router";
import {Location} from "@angular/common";
import {CacheService} from "../cache.service";
import {ContentService} from "../content.service";
import {DropViewComponent} from "../drop-view/drop-view.component";
import {DropTriggerComponent} from "../drop-trigger/drop-trigger.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-trigger',
  templateUrl: './trigger.component.html',
  styleUrls: ['./trigger.component.css']
})
export class TriggerComponent implements OnInit {

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

  confirmDrop(): void {
    const databaseId = this.cacheService.currentDatabase?.id
    const schemaId = this.cacheService.currentSchema?.id
    const schemaName = this.cacheService.currentSchema?.name
    if (databaseId != null && schemaId != null) {
      const dialogRef = this.dialog.open(DropTriggerComponent, {data: {name: schemaName}});
      dialogRef.afterClosed().subscribe(confirmed => {
        if (confirmed) {
          this.dropTrigger(databaseId, schemaId)
        }
      });
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
        .triggerById(databaseId, schemaId)
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

  private dropTrigger(databaseId: string, schemaId: string) {
    this.contentService
      .dropTriggerById(databaseId, schemaId)
      .subscribe(_ => this.back())
  }
}

import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Location} from '@angular/common';
import {SchemaService} from "../schema.service";
import {SchemaCell} from "../schema_cell";
import {DatabaseService} from "../database.service";
import {DomSanitizer} from "@angular/platform-browser";
import {MatIconRegistry} from "@angular/material/icon";
import {CacheService} from "../cache.service";

@Component({
  selector: 'app-schema',
  templateUrl: './schema.component.html',
  styleUrls: ['./schema.component.css']
})
export class SchemaComponent implements OnInit {

  databaseName?: string;

  tables: SchemaCell[] = []
  views: SchemaCell[] = []
  triggers: SchemaCell[] = []

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private location: Location,
    private schemaService: SchemaService,
    private databaseService: DatabaseService,
    private cacheService: CacheService,
    private matIconRegistry: MatIconRegistry,
    private domSanitizer: DomSanitizer
  ) {
    this.matIconRegistry.addSvgIcon(
      "table",
      this.domSanitizer.bypassSecurityTrustResourceUrl("../assets/table.svg")
    );
  }

  ngOnInit(): void {
    this.fetchAll()
  }

  back(): void {
    this.cacheService.currentDatabase = null
    this.location.back();
  }

  showTable(schema: SchemaCell) {
    this.cacheService.currentSchema = schema
    const databaseId = String(this.route.snapshot.paramMap.get('database_id'));
    this.router.navigateByUrl(`databases/${databaseId}/tables/${schema.id}`)
  }

  showView(schema: SchemaCell) {
    this.cacheService.currentSchema = schema
    const databaseId = String(this.route.snapshot.paramMap.get('database_id'));
    this.router.navigateByUrl(`databases/${databaseId}/views/${schema.id}`)
  }

  showTrigger(schema: SchemaCell) {
    this.cacheService.currentSchema = schema
    const databaseId = String(this.route.snapshot.paramMap.get('database_id'));
    this.router.navigateByUrl(`databases/${databaseId}/triggers/${schema.id}`)
  }

  private fetchAll() {
    const name = this.cacheService.currentDatabase?.name
    if (name != null) {
      this.databaseName = name
    }
    const databaseId = String(this.route.snapshot.paramMap.get('database_id'));
    if (databaseId != null) {
      this.schemaService.tablesById(databaseId).subscribe(tables => this.tables = tables.cells)
      this.schemaService.viewsById(databaseId).subscribe(views => this.views = views.cells)
      this.schemaService.triggersById(databaseId).subscribe(triggers => this.triggers = triggers.cells)
    }
  }
}

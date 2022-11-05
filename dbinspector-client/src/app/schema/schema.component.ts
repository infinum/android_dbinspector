import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Location} from '@angular/common';
import {SchemaService} from "../schema.service";
import {SchemaCell} from "../schema_cell";
import {DatabaseService} from "../database.service";
import {DomSanitizer} from "@angular/platform-browser";
import {MatIconRegistry} from "@angular/material/icon";

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
    private route: ActivatedRoute,
    private location: Location,
    private schemaService: SchemaService,
    private databaseService: DatabaseService,
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

  private fetchAll() {
    const databaseId = String(this.route.snapshot.paramMap.get('id'));
    if (databaseId != null) {
      this.databaseService.fromCacheById(databaseId).subscribe(database => this.databaseName = database.name);
      this.schemaService.tablesById(databaseId).subscribe(tables => this.tables = tables.cells)
      this.schemaService.viewsById(databaseId).subscribe(views => this.views = views.cells)
      this.schemaService.triggersById(databaseId).subscribe(triggers => this.triggers = triggers.cells)
    }
  }

  back(): void {
    this.location.back();
  }
}

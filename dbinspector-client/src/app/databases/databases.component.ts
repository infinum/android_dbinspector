import {Component, OnInit} from '@angular/core';
import {MatBottomSheet} from '@angular/material/bottom-sheet';
import {Router} from '@angular/router';
import {DatabaseService} from "../database.service";
import {DeleteDatabaseSheetComponent} from "../delete-database-sheet/delete-database-sheet.component";
import {RenameDatabaseSheetComponent} from "../rename-database-sheet/rename-database-sheet.component";
import {Database} from "../database";
import {saveAs} from 'file-saver';
import {CacheService} from "../cache.service";

@Component({
  selector: 'app-databases',
  templateUrl: './databases.component.html',
  styleUrls: ['./databases.component.css']
})
export class DatabasesComponent implements OnInit {

  databases: Database[] = [];

  constructor(
    private router: Router,
    private databaseService: DatabaseService,
    private cacheService: CacheService,
    private bottomSheet: MatBottomSheet
  ) {
  }

  ngOnInit(): void {
    this.fetchAll()
  }

  private fetchAll(): void {
    this.databaseService.fetchAll()
      .subscribe(
        databases => this.databases = databases
      )
  }

  showDeleteSheet(database: Database) {
    this.bottomSheet.open(
      DeleteDatabaseSheetComponent,
      {
        data: {id: database.id, name: database.name},
      });
  }

  showEditSheet(database: Database) {
    this.bottomSheet.open(
      RenameDatabaseSheetComponent,
      {
        data: {id: database.id, name: database.name},
      });
  }

  duplicateDatabase(database: Database) {
    this.databaseService.copyById(database.id).subscribe(_ => this.fetchAll())
  }

  downloadDatabase(database: Database) {
    this.databaseService.downloadById(database.id).subscribe(blob =>
      saveAs(blob, database.path.split(/[\\\/]/).pop())
    )
  }

  showSchema(database: Database) {
    this.cacheService.currentDatabase = database
    this.router.navigateByUrl(`databases/${database.id}/schema`)
  }
}

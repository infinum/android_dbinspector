import {Component, OnInit} from '@angular/core';
import {DatabaseService} from "../database.service";
import {Database} from "../database";
import {MatBottomSheet} from '@angular/material/bottom-sheet';
import {DeleteDatabaseSheetComponent} from "../delete-database-sheet/delete-database-sheet.component";
import {saveAs} from 'file-saver';

@Component({
  selector: 'app-databases',
  templateUrl: './databases.component.html',
  styleUrls: ['./databases.component.css']
})
export class DatabasesComponent implements OnInit {

  databases: Database[] = [];

  constructor(
    private databaseService: DatabaseService,
    private bottomSheet: MatBottomSheet
  ) {
  }

  ngOnInit(): void {
    this.fetchAll()
  }

  fetchAll(): void {
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

  duplicateDatabase(database: Database) {
    this.databaseService.copyById(database.id).subscribe(_ => this.fetchAll())
  }

  downloadDatabase(database: Database) {
    this.databaseService.downloadById(database.id).subscribe(blob =>
      saveAs(blob, database.path.split(/[\\\/]/).pop())
    )
  }
}

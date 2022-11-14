import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {DatabaseService} from "../database.service";
import {DeleteDatabaseComponent} from "../delete-database/delete-database.component";
import {RenameDatabaseComponent} from "../rename-database/rename-database.component";
import {Database} from "../database";
import {saveAs} from 'file-saver';
import {CacheService} from "../cache.service";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-databases',
  templateUrl: './databases.component.html',
  styleUrls: ['./databases.component.css']
})
export class DatabasesComponent implements OnInit {

  databases: Database[] = [];
  searchTerm = '';
  isSearching = false;

  constructor(
    private router: Router,
    private databaseService: DatabaseService,
    private cacheService: CacheService,
    private dialog: MatDialog
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

  github(): void {
    window.open("https://github.com/infinum/android_dbinspector", "_blank");
  }

  startSearch(): void {
    this.isSearching = true
  }

  endSearch(): void {
    this.isSearching = false
  }

  confirmDelete(event: MouseEvent, database: Database) {
    event.stopPropagation()
    const dialogRef = this.dialog.open(DeleteDatabaseComponent, {data: {name: database.name}});
    dialogRef.afterClosed().subscribe(confirmed => {
      if (confirmed) {
        this.deleteDatabase(database.id)
      }
    });
  }

  confirmEdit(event: MouseEvent, database: Database) {
    event.stopPropagation()
    const dialogRef = this.dialog.open(RenameDatabaseComponent, {data: {name: database.name}});
    dialogRef.afterClosed().subscribe(result => {
      if (result.confirmed) {
        this.renameDatabase(database.id, result.newName)
      }
    });
  }

  duplicateDatabase(event: MouseEvent, database: Database) {
    event.stopPropagation()
    this.databaseService.copyById(database.id).subscribe(_ => this.fetchAll())
  }

  downloadDatabase(event: MouseEvent, database: Database) {
    event.stopPropagation()
    this.databaseService.downloadById(database.id).subscribe(blob =>
      saveAs(blob, database.path.split(/[\\\/]/).pop())
    )
  }

  showSchema(database: Database) {
    this.cacheService.currentDatabase = database
    this.router.navigateByUrl(`databases/${database.id}/schema`)
  }

  private deleteDatabase(databaseId: string): void {
    this.databaseService.deleteById(databaseId).subscribe(_ =>
      this.fetchAll()
    )
  }

  private renameDatabase(databaseId: string, newName: string) {
    this.databaseService.renameById(databaseId, newName).subscribe(_ =>
      this.fetchAll()
    )
  }
}

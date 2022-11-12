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

  constructor(
    private router: Router,
    private databaseService: DatabaseService,
    private cacheService: CacheService,
    public dialog: MatDialog
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

  showDeleteSheet(database: Database) {
    const dialogRef = this.dialog.open(DeleteDatabaseComponent, {data: {id: database.id, name: database.name}});
    dialogRef.afterClosed().subscribe(confirmed => {
      if (confirmed) {
        this.deleteDatabase(database.id)
      }
    });
  }

  showEditSheet(database: Database) {
    const dialogRef = this.dialog.open(RenameDatabaseComponent, {data: {id: database.id, name: database.name}});
    dialogRef.afterClosed().subscribe(result => {
      if (result.confirmed) {
        this.renameDatabase(database.id, result.newName)
      }
    });
  }

  deleteDatabase(databaseId: string): void {
    this.databaseService.deleteById(databaseId).subscribe()
  }

  renameDatabase(databaseId: string, newName: string) {
    this.databaseService.renameById(databaseId, newName).subscribe()
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
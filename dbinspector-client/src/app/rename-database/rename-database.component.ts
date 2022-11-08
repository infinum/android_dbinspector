import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-rename-database',
  templateUrl: './rename-database.component.html',
  styleUrls: ['./rename-database.component.css']
})
export class RenameDatabaseComponent {

  private readonly databaseId?: string;
  databaseName?: string;

  newName?: string

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { id: string, name: string },
    public dialogRef: MatDialogRef<RenameDatabaseComponent>
  ) {
    this.databaseId = data.id
    this.databaseName = data.name
    this.newName = data.name
  }

  onCancel(): void {
    this.dialogRef.close({confirmed: false, newName: undefined})
  }

  onConfirm(): void {
    this.dialogRef.close({confirmed: true, newName: this.newName})
  }
}

import {Component, Inject} from '@angular/core';
import {MatBottomSheetRef} from '@angular/material/bottom-sheet';
import {MAT_BOTTOM_SHEET_DATA} from '@angular/material/bottom-sheet';
import {DatabaseService} from "../database.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-delete-database',
  templateUrl: './delete-database.component.html',
  styleUrls: ['./delete-database.component.css']
})
export class DeleteDatabaseComponent {

  private readonly databaseId?: string;
  databaseName?: string;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { id: string, name: string },
    public dialogRef: MatDialogRef<DeleteDatabaseComponent>
  ) {
    this.databaseId = data.id
    this.databaseName = data.name
  }

  onCancel(): void {
    this.dialogRef.close(false)
  }

  onConfirm(): void {
    this.dialogRef.close(true)
  }
}

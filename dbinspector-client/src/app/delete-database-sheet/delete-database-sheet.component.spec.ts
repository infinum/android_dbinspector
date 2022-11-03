import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteDatabaseSheetComponent } from './delete-database-sheet.component';

describe('DeleteDatabaseSheetComponent', () => {
  let component: DeleteDatabaseSheetComponent;
  let fixture: ComponentFixture<DeleteDatabaseSheetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DeleteDatabaseSheetComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeleteDatabaseSheetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

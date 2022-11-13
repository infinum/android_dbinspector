import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClearTableComponent } from './clear-table.component';

describe('ClearTableComponent', () => {
  let component: ClearTableComponent;
  let fixture: ComponentFixture<ClearTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClearTableComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClearTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DropTriggerComponent } from './drop-trigger.component';

describe('DropTriggerComponent', () => {
  let component: DropTriggerComponent;
  let fixture: ComponentFixture<DropTriggerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DropTriggerComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DropTriggerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

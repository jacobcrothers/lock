import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { LocksComponent } from './locks.component';

describe('LocksComponent', () => {
  let component: LocksComponent;
  let fixture: ComponentFixture<LocksComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ LocksComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LocksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

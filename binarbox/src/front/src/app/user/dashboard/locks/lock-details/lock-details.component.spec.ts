import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LockDetailsComponent } from './lock-details.component';

describe('LockDetailsComponent', () => {
  let component: LockDetailsComponent;
  let fixture: ComponentFixture<LockDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LockDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LockDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

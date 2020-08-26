import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddLockComponent } from './add-lock.component';

describe('AddLockComponent', () => {
  let component: AddLockComponent;
  let fixture: ComponentFixture<AddLockComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddLockComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddLockComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

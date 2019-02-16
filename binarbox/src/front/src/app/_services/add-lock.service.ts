import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AddLockService {

  private lockTypesUrl     = 'lock/category';
  private saveLockUrl      = 'lock';
  private savePanelcSectionUrl = 'lock/{lockId}/section/{sectionId}'

  public createdLock: any;

  constructor(
    private http: HttpClient,
    private router: Router
  ) { }

  getLockTypes() {
      return this.http.get(this.lockTypesUrl, {});
  }

  saveLock(lock) {
    this.createdLock = lock;
    this.savePanelcSectionUrl = this.savePanelcSectionUrl.replace('{lockId}', this.createdLock.lockTemplate);
    return this.http.post(this.saveLockUrl, lock);
  }

  savePanelSection(sectionId) {
    this.savePanelcSectionUrl = this.savePanelcSectionUrl.replace('{sectionId}', sectionId);
    return this.http.put(this.savePanelcSectionUrl, {});
  }
}

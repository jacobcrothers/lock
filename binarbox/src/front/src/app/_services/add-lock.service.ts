import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {UserService} from './user.service';


@Injectable({
    providedIn: 'root'
})
export class AddLockService {

    public createdLock: any;
    private lockTypesUrl = 'lock/category';
    private saveLockUrl = 'lock';
    private getLocksByUserIdUrl = 'lock/{userId}';
    private saveLockSectionUrl = 'lock/{lockId}/section/{sectionId}';
    private _lockId;

    constructor(
        private http: HttpClient,
        private router: Router
    ) {
    }

    getLockTypes() {
        return this.http.get(this.lockTypesUrl, {});
    }


    getLockId() {
        return this._lockId;
    }


    setLockId(response) {
        this._lockId = response.id;
    }

    saveLock(lock) {
        this.createdLock = lock;
        let userId = UserService.getUserId();
        lock.userId = userId;
        console.log(lock, "++++");
        return this.http.post(this.saveLockUrl, lock);
    }


    savePanelSection(sectionId: Number) {
        this.saveLockSectionUrl = this.saveLockSectionUrl
            .replace('{lockId}', this._lockId)
            .replace('{sectionId}', sectionId.toString());
        return this.http.put(this.saveLockSectionUrl, {});
    }

    getLocksByUserId() {
        let userId = UserService.getUserId();
        console.log(userId, "++++");
        this.getLocksByUserIdUrl = this.getLocksByUserIdUrl
            .replace('{userId}', userId);
        return this.http.get(this.getLocksByUserIdUrl, {});
    }
}

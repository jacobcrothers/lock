import { Component, OnInit } from '@angular/core';


@Component({
  selector: 'app-video-player',
  templateUrl: './video-player.component.html',
  styleUrls: ['./video-player.component.scss']
})
export class VideoPlayerComponent implements OnInit {

    public videoUrl: String;
    public headers: String;

  constructor() { }

  ngOnInit() {
      this.videoUrl = "https://dbdee3ttou8ph.cloudfront.net/1/hls2m/hls2m.m3u8";
      this.headers = "headers"
  }

}

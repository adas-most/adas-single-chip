# WEB-RTC-VideoRecording

透過網頁平台，進行擷取 web camera 的影像資料，錄製影片與播放直播

這是一個 node.js 專案

它透過WEB RTC技術與網頁平台對攝影機（web camera）的影像進行擷取並存成影片檔

使用方式只要透過指令

  ＄node server.js 
  
即可打開網頁伺服器

之後只要在瀏覽器中輸入

進行影像錄製 ： https://127.0.0.1:8787/LiveShow 

進行影像播放 ： https://127.0.0.1:8787/Viewers 

影像檔案則會存入 \temp 資料夾中

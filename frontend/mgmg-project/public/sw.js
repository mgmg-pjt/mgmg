const CACHE_NAME = "version-7";
const EX_CACHE_LIST = [
  "version-1",
  "version-2",
  "version-3",
  "version-4",
  "version-5",
  "version-6",
];

const urlsToCache = [
  "/",
  "index.html",
  "favicon.ico",
  "/img/icons/msapplication-icon-144x144.png",
  "/img/icons/mstile-150x150.png",
  "/img/icons/android-chrome-192x192.png",
  "/img/icons/android-chrome-512x512.png",
  "/img/icons/android-chrome-maskable-192x192.png",
  "/img/icons/android-chrome-maskable-512x512.png",
  "/img/icons/apple-touch-icon-60x60.png",
  "/img/icons/apple-touch-icon-76x76.png",
  "/img/icons/apple-touch-icon-120x120.png",
  "/img/icons/apple-touch-icon-152x152.png",
  "/img/icons/apple-touch-icon-180x180.png",
  "../src/assets/font/font.css",
  "../src/styles/App.css",
  "../src/styles/diary/BackgroundChoice.css",
  "../src/styles/diary/DiaryStyle.css",
  "../src/styles/diary/DiaryWritingPage.css",
  "../src/styles/diary/GiftRecommend.css",
  "../src/styles/diary/LodingView.css",
  "../src/styles/diary/MusicRecommend.css",
];

// cache 목록 등록 및 install 시 cache 목록이 다운
self.addEventListener("install", (event) => {
  event.waitUntil(async () => {
    const cache = await caches.open(CACHE_NAME);
    console.log("Opened cache");
    await cache.addAll(urlsToCache);
  });
});

// keep fetching the requests from the user
self.addEventListener("fetch", (event) => {
  event.respondWith(
    (async () => {
      const r = await caches.match(event.request);
      if (r) return r;
      const response = await fetch(event.request);
      if (event.request.method !== "POST") {
        const cache = await caches.open(CACHE_NAME);
        cache.put(event.request, response.clone());
      }
      return response;
    })()
  );
});

self.addEventListener("activate", function (event) {
  const cacheWhitelist = []; // add cache names which you do not want to delete
  cacheWhitelist.push(CACHE_NAME);
  event.waitUntil(
    caches.keys().then((cacheNames) => {
      return Promise.all(
        cacheNames
          .filter((key) => {
            return EX_CACHE_LIST.includes(key);
          })
          .map((key) => {
            return caches.delete(key);
          })
      );
    })
  );
});

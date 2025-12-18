<template id="feed-view">
  <app-frame>
    <h1>Latest photos</h1>
    <photo-gallery take="8"></photo-gallery>
  </app-frame>
</template>
<script nonce="@addNonce">
Vue.component("feed-view", { template: "#feed-view" });
</script>

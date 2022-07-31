<template id="my-photos-view">
  <app-frame>
    <h1>My photos</h1>
    <photo-upload-form></photo-upload-form>
    <photo-gallery :forUser="true"></photo-gallery>
  </app-frame>
</template>
<script>
Vue.component("my-photos-view", { template: "#my-photos-view" });
</script>

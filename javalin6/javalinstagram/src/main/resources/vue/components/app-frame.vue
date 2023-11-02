<template id="app-frame">
  <v-app>
    <v-main>
      <nav-bar></nav-bar>
      <v-container>
        <slot></slot>
      </v-container>
    </v-main>
  </v-app>
</template>
<script nonce="@addNonce">
Vue.component("app-frame", { template: "#app-frame" });
</script>
<style>
@media only screen and (min-width: 1600px) {
  .container {
    max-width: 1200px;
  }
}
</style>

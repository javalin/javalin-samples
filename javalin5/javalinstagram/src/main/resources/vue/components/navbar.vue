<template id="nav-bar">
  <header class="navbar">
    <v-container class="pa-0">
      <v-toolbar flat>
        <a href="/">
          <img class="logo-img" src="/logo.png" alt="Javalinstagram"/>
        </a>
        <a href="/"><span class="logo-text">Javalinstagram</span></a>
        <v-spacer></v-spacer>
        <v-toolbar-items v-if="$javalin.state.currentUser">
          <v-menu bottom left offset-y>
            <template v-slot:activator="{ on }">
              <v-btn v-on="on" text>
                <v-icon color="grey darken-2" size="30">account_circle</v-icon>
              </v-btn>
            </template>
            <v-list>
              <v-list-item>
                <small>Signed in as '{{ $javalin.state.currentUser }}'</small>
              </v-list-item>
              <v-list-item>
                <a href="/my-photos">My photos</a>
              </v-list-item>
              <v-list-item @click="signOut">Sign out</v-list-item>
            </v-list>
          </v-menu>
        </v-toolbar-items>
      </v-toolbar>
    </v-container>
  </header>
</template>
<script>
Vue.component("nav-bar", {
  template: "#nav-bar",
  methods: {
    signOut() {
      axios.post("/api/account/sign-out").then(() => {
        location.reload();
      });
    },
  },
});
</script>
<style>
.navbar {
  border-bottom: 1px solid #ddd;
}

.navbar .logo-img {
  height: 44px;
  display: block;
  user-select: none;
}

.navbar .logo-text {
  margin-left: 16px;
  font-family: "Lobster Two", cursive;
  font-weight: 400;
  color: #444;
  font-size: 28px;
  user-select: none;
}
</style>

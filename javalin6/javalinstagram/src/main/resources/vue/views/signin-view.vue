<template id="signin-view">
  <app-frame>
    <v-card class="signin-card">
      <v-card-text>
        <h1 v-if="isSignin">Sign in</h1>
        <h1 v-else>Create account</h1>
        <v-alert v-model="errorAlert" dismissible type="error" class="mb-3">
          {{ errorMessage }}
        </v-alert>
        <v-text-field
          v-model="username"
          outlined
          label="Username"
          append-icon="mdi-account"
        ></v-text-field>
        <v-text-field
          v-model="password"
          @keyup.enter="signInOrUp"
          outlined
          type="password"
          label="Password"
          append-icon="mdi-lock"
        ></v-text-field>
        <v-btn
          v-if="isSignin"
          @click="signInOrUp"
          depressed
          block
          large
          color="primary"
          >Sign in</v-btn
        >
        <v-btn
          v-if="!isSignin"
          @click="signInOrUp"
          depressed
          block
          large
          color="primary"
          >Create account</v-btn
        >
        <div class="mt-3">
          <div v-if="isSignin">
            Need an account? <a @click="switchForm">Register</a>
          </div>
          <div v-if="!isSignin">
            Have an account? <a @click="switchForm">Sign in</a>
          </div>
        </div>
      </v-card-text>
    </v-card>
  </app-frame>
</template>
<script nonce="@addNonce">
Vue.component("signin-view", {
  template: "#signin-view",
  data: () => ({
    errorAlert: false,
    errorMessage: "",
    isSignin: true,
    username: "",
    password: "",
  }),
  methods: {
    signInOrUp() {
      let url = "/api/account/" + (this.isSignin ? "sign-in" : "sign-up");
      axios
        .post(url, { username: this.username, password: this.password })
        .then(() => {
          window.location = "/";
        })
        .catch((error) => {
          this.errorMessage = error.response.data.title;
          this.errorAlert = true;
        });
    },
    switchForm: function () {
      this.isSignin = !this.isSignin;
    },
  },
});
</script>
<style>
.signin-card {
  max-width: 360px;
  margin: 50px auto;
}

.signin-card h1 {
  font-family: "Lobster Two", cursive;
  font-weight: 400;
  color: #444;
  font-size: 28px;
  text-align: center;
  margin-bottom: 16px;
}
</style>

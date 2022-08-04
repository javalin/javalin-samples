<template id="photo-upload-form">
  <div class="photo-upload-form">
    <div class="photo-upload-droparea">
      <div>Drag or click to choose photo</div>
      <form id="upload-form" ref="uploadForm">
        <input
          class="photo-upload-input"
          ref="uploadInput"
          @change="previewPhoto"
          name="photo"
          type="file"
          accept="image/*"
        />
      </form>
    </div>
    <v-dialog v-model="dialog" max-width="460" persistent>
      <v-card>
        <v-card-title class="headline">Publish photo?</v-card-title>
        <v-card-text>
          <img :src="photopreview" class="photo-upload-preview" />
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="gray darken-2" text @click="clearUploadForm">Cancel</v-btn>
          <v-btn color="primary" @click="uploadPhoto">Publish</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>
<script>
Vue.component("photo-upload-form", {
  template: "#photo-upload-form",
  data: () => ({
    photopreview: null,
  }),
  methods: {
    previewPhoto() {
      if (this.$refs.uploadInput.files[0]) {
        const reader = new FileReader();
        reader.onload = (e) => (this.photopreview = e.target.result);
        reader.readAsDataURL(this.$refs.uploadInput.files[0]);
      }
    },
    clearUploadForm() {
      this.$refs.uploadInput.value = "";
      this.photopreview = null;
    },
    uploadPhoto() {
      const data = new FormData(this.$refs.uploadForm);
      const config = { headers: { "Content-Type": "multipart/form-data" } };
      axios
        .post("/api/photos", data, config)
        .then(() => {
          this.clearUploadForm();
          this.$eventBus.$emit("photo-uploaded");
        })
        .catch((error) => {
          alert("An error occurred while uploading the photo.");
          console.log(error);
        });
    },
  },
  computed: {
    dialog: function () {
      return this.photopreview !== null;
    },
  },
});
</script>
<style>
.photo-upload-droparea {
  position: relative;
  width: 100%;
  height: 120px;
  line-height: 120px;
  text-align: center;
  font-size: 18px;
  border: 3px dashed #ddd;
  background: #fff;
  margin-bottom: 32px;
}
.photo-upload-input {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 100%;
  opacity: 0;
}
.photo-upload-preview {
  width: 428px;
  height: 428px;
  max-width: 100%;
  object-fit: cover;
}
</style>

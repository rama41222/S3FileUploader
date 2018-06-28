<template>
  <section class="container">
    <div>
      <b-row align="center">
        <b-col >
          <pre-loader :spin="shouldSpin"></pre-loader>
        </b-col>
      </b-row>
      <h1 class="title">
        rum j
      </h1>
      <h2 class="subtitle">
        The Nextgen Storage Service
      </h2>
      <div class="links">
        <b-row align-h="center" class="row-pad">
          <b-col cols="12" align="center">
            <form data-vv-scope="fileUploadForm">

              <b-form-file style="cursor: pointer" v-model="fileForm.file" :state="Boolean(fileForm.file)"
                           placeholder="Choose a file..."></b-form-file>
              <br/>

              <b-row class="row-pad" align-h="center">
                <b-button variant="outline-primary" @click="upload">Upload</b-button>
              </b-row>
            </form>
          </b-col>
        </b-row>

        <div class="floater">
          <b-button class="outline-primary-floater" @click="showModal">View All</b-button>
        </div>
      </div>
    </div>

    <b-modal ref="viewAllModal" hide-footer title="All My Files" size="lg">
      <div class="d-block">
        <div v-for="item in uploadedFiles" :key="item.id" class="img-comp">
          <b-row>
              <b-col>
                <img :src="item.url" class="img_preview"/>

              </b-col>
          </b-row>
          <b-row>
            <b-col>
              <p class="img_text">{{item.name}}</p>
            </b-col>
          </b-row>
        </div>
      </div>
      <b-btn class="mt-3" variant="outline-danger" block @click="hideModal">Close</b-btn>
    </b-modal>

  </section>
</template>

<script>

  export default {
    async beforeMount() {
    },
    async mounted() {
      await this.fetchFiles()
    },
    data() {
      return {
        fileForm: {
          file: null,
        },
        uploadedFiles:[],
        shouldSpin: false
      }
    },
    components: {},
    methods: {
      showModal () {
        this.$refs.viewAllModal.show()
      },
      hideModal () {
        this.$refs.viewAllModal.hide()
      },
      async fetchFiles(){
        const headers = {
          'Accept': 'application/json',
          'Content-type': 'application/json'
        }
        await this.$axios.$get('/files', {headers: headers}).then( res=>{
          this.uploadedFiles = res

        }).catch(e =>{
          console.log(e)
        })


      },
      async upload(e){
        e.preventDefault()

        if(typeof this.fileForm.file === 'undefined' || !this.fileForm.file){
          this.$toast.error("Please select a file to upload", {icon: 'error' })
          return
        }
        this.shouldSpin = true
        let formData = new FormData()
        formData.append('file', this.fileForm.file)

        try {

          const headers = {
            'Accept': 'application/json',
            'Content-type': 'multipart/form-data'
          }

          const resp = await this.$axios.$post('/files', formData, {headers: headers})

          if(resp.error) {
            this.$toast.error("Error while uploading, You may try again!", {icon:'error_outline'})
            return
          }
          this.$toast.success("File uploaded Successfully", {icon:'done_outline'})
          this.fileForm.file = null
        } catch (e) {
          this.$toast.error("Error while uploading, You may try again!", {icon:'error_outline'})
        } finally {
          await this.fetchFiles()
          this.shouldSpin = false
        }

      }
    }
  }
</script>

<style>
  .container {
    min-height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
    text-align: center;
  }

  .title {
    font-family: "Quicksand", "Source Sans Pro", -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif; /* 1 */
    display: block;
    font-weight: 300;
    font-size: 100px;
    color: #35495e;
    letter-spacing: 1px;
  }

  .subtitle {
    font-weight: 300;
    font-size: 42px;
    color: #526488;
    word-spacing: 5px;
    padding-bottom: 15px;
  }

  .row-pad {
    margin-top: 15px;
  }

  .floater {
    position: absolute;
    bottom: 2vh;
    right: 2vh;
  }

  .outline-primary-floater {
    background-color: white !important;
    border-color: #45baff !important;
    color: #6187b9;
    border-radius: 100% !important;
    padding: 20px;
    width: 100px;
    height: 100px;
  }

  .img_preview {
    margin: 10px;
    border-radius: 10px;
    object-fit: cover;
    object-position: center;
    height: 100px;
    width: 100px;
  }

  .outline-primary-floater:hover {
    background-color: #45baff !important;
    border-color: white !important;
    color: white;
  }

  .file-selector {
    cursor: pointer!important;
  }

  .img-comp {
    display: inline-block;
    height: auto;
    position: relative;
    padding: 10px;
    margin: 10px;
    cursor: pointer;
    border-radius: 20px;
    border: 1px solid #45baff;
    overflow: hidden;
  }

  .img_text {
    overflow: hidden !important;
    text-overflow: ellipsis !important;
    float: none !important;
    display:inline-block;
    font-size: 0.6em;
  }
</style>

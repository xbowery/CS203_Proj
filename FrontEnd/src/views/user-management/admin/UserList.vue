<template>
<v-card>
    <v-card-text class="d-flex align-center flex-wrap pb-0">
    <!-- search -->
      <v-text-field
        v-model="search"
        placeholder="Search"
        outlined
        hide-details
        dense
        class="user-search me-3 mb-4"
        label="Search"
      >
      </v-text-field>

      <v-spacer></v-spacer>
    </v-card-text>
  <v-data-table
    :headers="headers"
    :items="desserts"
    :search = "search"
    sort-by="username"
    class="elevation-1"
  >
    <template v-slot:top>
      <v-toolbar
        flat
      >
        <v-toolbar-title>Users List</v-toolbar-title>
        <v-divider
          class="mx-4"
          inset
          vertical
        ></v-divider>
        <v-spacer></v-spacer>
        <v-dialog
          v-model="dialog"
          max-width="500px"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              color="primary"
              dark
              class="mb-2"
              v-bind="attrs"
              v-on="on"
            >
              Add New User
            </v-btn>
          </template>
          <validation-observer ref="obs">
          <v-card>
            <v-card-title>
              <span class="text-h5">{{ formTitle }}</span>
            </v-card-title>

            <v-card-text>
              <v-container>
                <v-row>
                  <v-col
                    cols="12"
                    sm="6"
                    md="4"
                  >
                  <validation-provider name="type" rules="required" v-slot="{ errors }">
                    <v-text-field
                      v-model="editedItem.Fullname"
                      :error-messages="errors[0]"
                      label="Full name"
                    ></v-text-field>
                  </validation-provider>
                  </v-col>
                  <v-col
                    cols="12"
                    sm="6"
                    md="4"
                  >
                  <validation-provider name="type" rules="required" v-slot="{ errors }">
                    <v-text-field
                      v-model="editedItem.username"
                      :error-messages="errors[0]"
                      label="Username"
                    ></v-text-field>
                  </validation-provider>
                  </v-col>
                  <v-col
                    cols="12"
                    sm="6"
                    md="4"
                  >
                  <validation-provider name="type" rules="required" v-slot="{ errors }">
                    <v-text-field
                      v-model="editedItem.email"
                      :error-messages="errors[0]"
                      label="Email"
                    ></v-text-field>
                  </validation-provider>
                  </v-col>
                  <v-col
                    cols="12"
                    sm="6"
                    md="4"
                  >
                  <validation-provider name="type" rules="required" v-slot="{ errors }">
                    <v-text-field
                      v-model="editedItem.company"
                      :error-messages="errors[0]"
                      label="Company"
                    ></v-text-field>
                  </validation-provider>
                  </v-col>
                  <v-col
                    cols="12"
                    sm="6"
                    md="4"
                  >
                  <validation-provider name="type" rules="required" v-slot="{ errors }">
                    <v-select
                      v-model="editedItem.role"
                      :error-messages="errors[0]"
                      label="Role"
                      :items="roleOptions"
                      item-text="title"
                        item-value="value"
                        outlined
                        dense
                        hide-details="auto"
                        class="mb-6"
                    ></v-select>
                  </validation-provider>
                  </v-col>
                </v-row>
              </v-container>
            </v-card-text>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                color="blue darken-1"
                text
                @click="close"
              >
                Cancel
              </v-btn>
              <v-btn
                color="blue darken-1"
                text
                @click="save"
              >
                Save
              </v-btn>
            </v-card-actions>
          </v-card>
          </validation-observer>
        </v-dialog>
        <v-dialog v-model="dialogDelete" max-width="500px">  
          <v-card>
            <v-card-title class="text-h5">Are you sure you want to delete this user?</v-card-title>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="closeDelete">Cancel</v-btn>
              <v-btn color="blue darken-1" text @click="deleteItemConfirm">OK</v-btn>
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-toolbar>
    </template>
    <template  #[`item.actions`]="{ item }">
      <!-- <v-icon
        small
        class="mr-2"
        @click="editItem(item)"
      >
        mdi-pencil
      </v-icon> -->
        <v-icon
              small
              color="secondary"
              outlined
              class="mr-2"

              @click="editItem(item)"
            >
              Edit
        </v-icon>

        <v-icon
              small
              color="secondary"
              outlined
              class="mr-2"

              @click="deleteItem(item)"
            >
              Delete
        </v-icon>
      <!-- <v-icon
        small
        @click="deleteItem(item)"
      >
        mdi-delete
      </v-icon> -->
    </template>
    <template v-slot:no-data>
      <v-btn
        color="primary"
        @click="initialize"
      >
        Reset
      </v-btn>
    </template>
  </v-data-table>
    </v-card>
</template>

<script>
import { ValidationProvider, ValidationObserver } from 'vee-validate'
  export default {
    components: { ValidationProvider, ValidationObserver },
    data: () => ({
      dialog: false,
      dialogDelete: false,
      search:'',
      errors:'',
      roleOptions: [
      { title: 'Admin', value: 'admin' },
      { title: 'User', value: 'user' },
      { title: 'Employee', value: 'employee' },
      { title: 'Business Owner', value: 'business owner' },
    ],
      headers: [
        {
          text: 'FULL NAME',
          align: 'start',
          sortable: false,
          value: 'Fullname',
        },
        { text: 'USERNAME', value: 'username' },
        { text: 'EMAIL', value: 'email' },
        { text: 'COMPANY', value: 'company' },
        { text: 'ROLE', value: 'role' },
        { text: 'ACTIONS', value: 'actions', sortable: true },
      ],
      desserts: [],
      editedIndex: -1,
      editedItem: {
        Fullname: '',
        username: '',
        email: '',
        company: '',
        role: '',
      },
      defaultItem: {
        Fullname: '',
        username: '',
        email: '',
        company: '',
        role: '',
      },
    }),

    computed: {
      formTitle () {
        return this.editedIndex === -1 ? 'New User' : 'Edit User'
      },
    },

    watch: {
      dialog (val) {
        val || this.close()
      },
      dialogDelete (val) {
        val || this.closeDelete()
      },
    },

    created () {
      this.initialize()
    },

    methods: {
      initialize () {
        this.desserts = [
          {
            Fullname: 'Frozen Yogurt',
            username: 159,
            email: 6.0,
            company: 24,
            role: 4.0,
          },
          {
            Fullname: 'Ice cream sandwich',
            username: 237,
            email: 9.0,
            company: 37,
            role: 4.3,
          },
          {
            Fullname: 'Eclair',
            username: 262,
            email: 16.0,
            company: 23,
            role: 6.0,
          },
          {
            Fullname: 'Cupcake',
            username: 305,
            email: 3.7,
            company: 67,
            role: 4.3,
          },
          {
            Fullname: 'Gingerbread',
            username: 356,
            email: 16.0,
            company: 49,
            role: 3.9,
          },
          {
            Fullname: 'Jelly bean',
            username: 375,
            email: 0.0,
            company: 94,
            role: 0.0,
          },
          {
            Fullname: 'Lollipop',
            username: 392,
            email: 0.2,
            company: 98,
            role: 0,
          },
          {
            Fullname: 'Honeycomb',
            username: 408,
            email: 3.2,
            company: 87,
            role: 6.5,
          },
          {
            Fullname: 'Donut',
            username: 452,
            email: 25.0,
            company: 51,
            role: 4.9,
          },
          {
            Fullname: 'KitKat',
            username: 518,
            email: 26.0,
            company: 65,
            role: 7,
          },
        ]
      },

      editItem (item) {
        this.editedIndex = this.desserts.indexOf(item)
        this.editedItem = Object.assign({}, item)
        this.dialog = true
      },

      deleteItem (item) {
        this.editedIndex = this.desserts.indexOf(item)
        this.editedItem = Object.assign({}, item)
        this.dialogDelete = true
      },

      deleteItemConfirm () {
        this.desserts.splice(this.editedIndex, 1)
        this.closeDelete()
      },

      close () {
        this.dialog = false
        this.$nextTick(() => {
          this.editedItem = Object.assign({}, this.defaultItem)
          this.editedIndex = -1
        })
      },

      closeDelete () {
        this.dialogDelete = false
        this.$nextTick(() => {
          this.editedItem = Object.assign({}, this.defaultItem)
          this.editedIndex = -1
        })
      },

      save () {
        if (this.editedIndex > -1) {
          Object.assign(this.desserts[this.editedIndex], this.editedItem)
        } else {
          this.desserts.push(this.editedItem)
        }
        this.close()
      },
    },
  }

</script>
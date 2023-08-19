<template>
  <div>
      <!--   调试弹窗     -->
      <el-dialog
              v-model="debugDialogVisible"
              title="调试"
              width="30%"
              destroy-on-close
              center
      >

          <el-form
                  label-position="right"
                  label-width="100px"
                  :model="debuggerForm"
                  style="max-width: 460px"
          >
              <el-form-item label="API标识">
                  <el-input disabled v-model="debuggerForm.apiId"/>
              </el-form-item>
              <el-form-item label="API路径">
                  <el-input v-model="debuggerForm.apiPath"/>
              </el-form-item>
              <el-form-item label="API方法">
                  <el-input v-model="debuggerForm.apiMethod"/>
              </el-form-item>
              <el-form-item label="请求头">
                  <div>
                      <el-row :gutter="20" v-for="(header, index) in debuggerForm.headers">
                          <el-col :span="8">
                              <el-input placeholder="参数名" v-model="header.name"/>
                          </el-col>
                          -
                          <el-col :span="10">
                              <el-input placeholder="参数值" v-model="header.value"/>
                          </el-col>
                          <el-col :span="3">
                              <el-button @click="removeHeaderItem(index)" type="primary" size="small" color="#fcd3d3" :icon="Minus" circle />
                          </el-col>
                      </el-row>
                      <el-button @click="addHeaderItem" type="primary" size="small" color="#a0cfff" :icon="Plus" circle />
                  </div>
              </el-form-item>
              <el-form-item label="请求参数">
                  <div>
                      <el-row :gutter="20" v-for="(param, index) in debuggerForm.params">
                          <el-col :span="8">
                              <el-input placeholder="参数名" v-model="param.name"/>
                          </el-col>
                          -
                          <el-col :span="10">
                              <el-input placeholder="参数值" v-model="param.value"/>
                          </el-col>
                          <el-col :span="3">
                              <el-button @click="removeParamItem(index)" type="primary" size="small" color="#fcd3d3" :icon="Minus" circle />
                          </el-col>
                      </el-row>
                      <el-button @click="addParamItem" type="primary" size="small" color="#a0cfff" :icon="Plus" circle />
                  </div>
              </el-form-item>
              <el-form-item label="请求体" v-if="debuggerForm.apiMethod == 'post' || debuggerForm.apiMethod == 'patch' || debuggerForm.apiMethod == 'put'">
                  <div>
                      <el-radio-group v-model="debuggerForm.contentType">
                          <el-radio label="formType">form-data</el-radio>
                          <el-radio label="rowType">row</el-radio>
                      </el-radio-group>
                      <div v-if="debuggerForm.contentType == 'formType'">
                          <el-row :gutter="20" v-for="(form, index) in debuggerForm.formBody">
                              <el-col :span="8">
                                  <el-input placeholder="参数名" v-model="form.name"/>
                              </el-col>
                              -
                              <el-col :span="10">
                                  <el-input placeholder="参数值" v-model="form.value"/>
                              </el-col>
                              <el-col :span="3">
                                  <el-button @click="removeFormBodyItem(index)" type="primary" size="small" color="#fcd3d3" :icon="Minus" circle />
                              </el-col>
                          </el-row>
                          <el-button style="alignment-baseline: baseline" @click="addFormBodyItem" type="primary" size="small" color="#a0cfff" :icon="Plus" circle />
                      </div>
                      <el-input v-else type="textarea" v-model="debuggerForm.rowBody"/>
                  </div>
              </el-form-item>
              <div style="width: 100%">
                  <el-button @click="sendRequest" style="width: 50%; margin-left: 25%" type="primary" plain>请  求</el-button>
              </div>
          </el-form>

          <template #footer>
      <span class="dialog-footer">
        <el-button @click="debugDialogVisible = false">取消</el-button>
      </span>
          </template>
      </el-dialog>
  </div>

</template>

<script setup>

import {Minus, Plus} from "@element-plus/icons";
import {ref} from "vue";
import {fetchGatewayApiByApiId} from "@/api/apis";

const debugDialogVisible = ref(false)
const debuggerForm = ref({
    apiId: String,
    apiPath: String,
    apiMethod: String,
    headers: [{
        name: String,
        value: String
    }],
    params: [{
        name: String,
        value: String
    }],
    contentType: String,
    formBody: [{
        name: String,
        value: String
    }],
    rowBody: String
})

const doDebugger = (index, row) => {
    debuggerForm.value.apiId = row.apiId;
    debuggerForm.value.formBody = []
    debuggerForm.value.params = []
    debuggerForm.value.headers = []
    debuggerForm.value.rowBody = ''
    if (row.apiId != null || row.apiId != '') {
        fetchGatewayApiByApiId(row.apiId).then((res) => {
            let data = res.data
            if (data.code == '0000') {
                debuggerForm.value.apiPath = data.data.apiPath
                debuggerForm.value.apiMethod = data.data.apiMethod
            }
            debugDialogVisible.value = true
            let names = []
            if (row.methodParametersName != null && row.methodParametersName != '') {
                names.push(...(row.methodParametersName.split(',')))
            }
            if (data.data.apiMethod == "post"
                || data.data.apiMethod == "patch"
                || data.data.apiMethod == "put") {
                names.forEach(name => {
                    debuggerForm.value.contentType = 'formType'
                    debuggerForm.value.formBody.push({
                        name: name,
                        value: ''
                    })
                })
            } else {
                names.forEach(name => {
                    debuggerForm.value.params.push({
                        name: name,
                        value: ''
                    })
                })
            }
        })
    }
}

const addHeaderItem = () => {
    debuggerForm.value.headers.push({
        name: '',
        value: ''
    })
}
const removeHeaderItem = (index) => {
    debuggerForm.value.headers.splice(index, 1)
}

const addParamItem = () => {
    debuggerForm.value.params.push({
        name: '',
        value: ''
    })
}
const removeParamItem = (index) => {
    debuggerForm.value.params.splice(index, 1)
}

const addFormBodyItem = () => {
    debuggerForm.value.formBody.push({
        name: '',
        value: ''
    })
}
const removeFormBodyItem = (index) => {
    debuggerForm.value.formBody.splice(index, 1)
}
</script>

<style scoped>

</style>

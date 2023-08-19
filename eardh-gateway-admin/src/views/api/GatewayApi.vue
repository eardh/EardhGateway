<template>
    <div>
        <PageDataView :="page"
                      @addData="form = {}"
                      @pageChange="handlePageChange"
                      @handleEdit="handleEdit"
                      @formSubmit="submit">
            <template v-slot:tableSlot>
                <el-table-column width="200" prop="apiId" label="API标识"/>
                <el-table-column width="300" prop="apiPath" label="API路径"/>
                <el-table-column width="100" prop="apiMethod" label="API方法"/>
                <el-table-column width="100" prop="apiProtocol" label="API协议"/>
                <el-table-column width="100" prop="apiAuth" label="API认证"/>
                <el-table-column show-overflow-tooltip prop="apiDescription" label="API描述"/>
            </template>

            <template v-slot:buttonSlot>
                <el-table-column fixed="right" label="限流" width="70">
                    <template #default="scope">
                        <el-button type="primary" size="small" :icon="Edit" color="#a0cfff"
                                   @click="handleRateLimiter(scope.$index, scope.row)"/>
                    </template>
                </el-table-column>

                <el-table-column fixed="right" label="白名单" width="70">
                    <template #default="scope">
                        <el-button type="primary" size="small" :icon="Edit" color="#a0cfff"
                                   @click="handleWhiteList(scope.$index, scope.row)"/>
                    </template>
                </el-table-column>

                <el-table-column fixed="right" label="黑名单" width="70">
                    <template #default="scope">
                        <el-button type="primary" size="small" :icon="Edit" color="#a0cfff"
                                   @click="handleBlackList(scope.$index, scope.row)"/>
                    </template>
                </el-table-column>
            </template>

            <template v-slot:formSlot>
                <el-form
                    label-position="right"
                    label-width="100px"
                    :model="form"
                    style="max-width: 460px"
                >
                    <el-form-item label="api标识">
                        <el-input disabled v-model="form.apiId"/>
                    </el-form-item>
                    <el-form-item label="api路径">
                        <el-input v-model="form.apiPath"/>
                    </el-form-item>
                    <el-form-item label="api方法">
                        <el-input v-model="form.apiMethod"/>
                    </el-form-item>
                    <el-form-item label="api协议">
                        <el-input v-model="form.apiProtocol"/>
                    </el-form-item>
                    <el-form-item label="是否认证">
                        <el-switch
                                v-model="form.apiAuth"
                                style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
                                inactive-value="0"
                                active-value="1"
                        />
                    </el-form-item>
                    <el-form-item label="api描述">
                        <el-input v-model="form.apiDescription"/>
                    </el-form-item>
                </el-form>
            </template>
        </PageDataView>

        <!--   限流弹窗     -->
        <el-dialog
            v-model="rateLimiterDialogVisible"
            title="编辑"
            width="30%"
            destroy-on-close
            center
        >

            <el-form
                label-position="right"
                label-width="100px"
                :model="rateLimiterForm"
                style="max-width: 460px"
            >
                <el-form-item label="API标识">
                    <el-input disabled v-model="rateLimiterForm.apiId"/>
                </el-form-item>
                <el-form-item label="限流规则">
                    <el-input-number v-model="rateLimiterForm.limiterRule" :precision="2"/>
                </el-form-item>
                <el-form-item label="令牌桶容量">
                    <el-input-number v-model="rateLimiterForm.bucketCapacity"/>
                </el-form-item>
                <el-form-item label="是否启用">
                    <el-switch
                        v-model="rateLimiterForm.forbidden"
                        style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
                        inactive-value="1"
                        active-value="0"
                    />
                </el-form-item>
            </el-form>

            <template #footer>
      <span class="dialog-footer">
        <el-button @click="rateLimiterDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRateLimiter">
          提交
        </el-button>
      </span>
            </template>
        </el-dialog>

        <!--   白名单弹窗     -->
        <el-dialog
            v-model="whiteListDialogVisible"
            title="编辑"
            width="30%"
            destroy-on-close
            center
        >

            <el-form
                label-position="right"
                label-width="100px"
                :model="whiteListForm"
                style="max-width: 460px"
            >
                <el-form-item label="API标识">
                    <el-input disabled v-model="whiteListForm.apiId"/>
                </el-form-item>
                <el-form-item label="白名单列表">
                    <el-input placeholder="IP之间用逗号隔开" type="textarea" v-model="whiteListForm.ipWhitelist"/>
                </el-form-item>
                <el-form-item label="是否启用">
                    <el-switch
                        v-model="whiteListForm.forbidden"
                        style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
                        inactive-value="1"
                        active-value="0"
                    />
                </el-form-item>
            </el-form>

            <template #footer>
      <span class="dialog-footer">
        <el-button @click="whiteListDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitWhiteList">
          提交
        </el-button>
      </span>
            </template>
        </el-dialog>

        <!--   黑名单弹窗     -->
        <el-dialog
            v-model="blackListDialogVisible"
            title="编辑"
            width="30%"
            destroy-on-close
            center
        >

            <el-form
                label-position="right"
                label-width="100px"
                :model="blackListForm"
                style="max-width: 460px"
            >
                <el-form-item label="API标识">
                    <el-input disabled v-model="blackListForm.apiId"/>
                </el-form-item>
                <el-form-item label="黑名单列表">
                    <el-input placeholder="IP之间用逗号隔开" type="textarea" v-model="blackListForm.ipBlacklist"/>
                </el-form-item>
                <el-form-item label="是否启用">
                    <el-switch
                        v-model="blackListForm.forbidden"
                        style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
                        inactive-value="1"
                        active-value="0"
                    />
                </el-form-item>
            </el-form>

            <template #footer>
      <span class="dialog-footer">
        <el-button @click="blackListDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitBlackList">
          提交
        </el-button>
      </span>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import PageDataView from "@/components/PageDataView.vue";
import {
    fetchBlackListData,
    fetchGatewayApiData,
    fetchRateLimiterData,
    fetchWhiteListData, updateBlackListData, updateGatewayApiData,
    updateRateLimiterData,
    updateWhiteListData
} from "@/api/apis";
import {ref} from "vue";
import {Edit} from "@element-plus/icons";
import {ElMessage} from "element-plus";
import {errorTip, successTip} from "@/utils/tool";

const page = {
    pageTitle: ref('api列表'),
    currentPage: ref(1),
    pageSize: ref(10),
    total: ref(0),
    tableData: ref([])
}

const form = ref({
    apiId: String,
    apiDescription: String,
    apiPath: String,
    apiMethod: String,
    apiProtocol: String,
    apiAuth: String
})


const getTableData = (current, size) => {
    fetchGatewayApiData(current, size).then((res) => {
        let data = res.data;
        if (data.code == '0000') {
            data = data.data;
            page.total.value = data.total
            page.tableData.value = []
            page.tableData.value.push(...data.items)
        }
    })
}

getTableData(page.currentPage.value, page.pageSize.value)

const handlePageChange = (val) => {
    getTableData(val, page.pageSize.value)
}

const handleEdit = (index, row) => {
    form.value = row
    form.value.apiAuth = String(row.apiAuth)
}


const submit = () => {
    updateGatewayApiData(form.value).then((res) => {
        let data = res.data;
        if (data.code == '0000') {
            successTip()
        } else {
            errorTip()
        }
        getTableData(page.currentPage.value, page.pageSize.value)
    })
}


// 下面为限流弹窗
const rateLimiterDialogVisible = ref(false)
const rateLimiterForm = ref({
    apiId: String,
    limiterRule: Number,
    bucketCapacity: Number,
    forbidden: String
})

const handleRateLimiter = (index, row) => {
    fetchRateLimiterData(row.apiId).then((res) => {
        let data = res.data
        if (data.code == '0000') {
            rateLimiterForm.value = {...data.data}
            if (data.data == null) {
                rateLimiterForm.value.forbidden = '1'
            } else {
                rateLimiterForm.value.forbidden = String(data.data.forbidden)
            }
        }
        rateLimiterForm.value.apiId = row.apiId
        rateLimiterDialogVisible.value = true
    })
}
const submitRateLimiter = () => {
    updateRateLimiterData(rateLimiterForm.value).then((res) => {
        let data = res.data;
        if (data.code == '0000') {
            successTip()
        } else {
            errorTip()
        }
        rateLimiterDialogVisible.value = false
    })
}

//下面为白名单弹窗
const whiteListDialogVisible = ref(false)
const whiteListForm = ref({
    apiId: String,
    ipWhitelist: String,
    forbidden: String
})
const handleWhiteList = (index, row) => {
    fetchWhiteListData(row.apiId).then((res) => {
        let data = res.data
        if (data.code == '0000') {
            whiteListForm.value = {...data.data}
            if (data.data == null) {
                whiteListForm.value.forbidden = '1'
            } else {
                whiteListForm.value.forbidden = String(data.data.forbidden)
            }
        }
        whiteListForm.value.apiId = row.apiId
        whiteListDialogVisible.value = true
    })
}
const submitWhiteList = () => {
    updateWhiteListData(whiteListForm.value).then((res) => {
        let data = res.data;
        if (data.code == '0000') {
            successTip()
        } else {
            errorTip()
        }
        whiteListDialogVisible.value = false
    })
}


//下面是黑名单
const blackListDialogVisible = ref(false)
const blackListForm = ref({
    apiId: String,
    ipBlacklist: String,
    forbidden: String
})
const handleBlackList = (index, row) => {
    fetchBlackListData(row.apiId).then((res) => {
        let data = res.data
        if (data.code == '0000') {
            blackListForm.value = {...data.data }
            if (data.data == null) {
                blackListForm.value.forbidden = '1'
            } else {
                blackListForm.value.forbidden = String(data.data.forbidden)
            }
        }
        blackListForm.value.apiId = row.apiId
        blackListDialogVisible.value = true
    })
}
const submitBlackList = () => {
    updateBlackListData(blackListForm.value).then((res) => {
        let data = res.data;
        if (data.code == '0000') {
            successTip()
        } else {
            errorTip()
        }
        blackListDialogVisible.value = false
    })
}
</script>

<style scoped></style>

package com.victoria.driver.ui.viewmodel


import com.victoria.driver.data.pojo.User
import com.victoria.driver.data.repository.UserRepository
import com.victoria.driver.ui.base.APILiveData
import com.victoria.driver.ui.base.BaseViewModel
import com.victoria.driver.ui.model.CarDocuments
import com.victoria.driver.ui.model.Parameter
import com.victoria.driver.ui.model.VehicleList
import okhttp3.RequestBody
import javax.inject.Inject

class SignUpViewModel @Inject constructor(private val userRepository: UserRepository) : BaseViewModel() {

    val signUpLiveData = APILiveData<User>()
    val vehicleListData = APILiveData<ArrayList<VehicleList>>()
    val addVehicleDetailData = APILiveData<User>()
    val uploadDocData = APILiveData<CarDocuments>()
    val bankData = APILiveData<User>()
    val addDocumentData = APILiveData<User>()
    val updateDocument = APILiveData<CarDocuments>()
    val updateVehicleDocument = APILiveData<User>()
    val updateBankData = APILiveData<User>()
    val updateDocumentEdit = APILiveData<User>()

    fun signUpUser(parameter: Parameter) {
        userRepository.signUp(parameter).subscribe(withLiveData(signUpLiveData))
    }

    fun vehicleListApi() {
        userRepository.vehicleList().subscribe(withLiveData(vehicleListData))
    }

    fun addVehicleDataApi(parameter: Parameter) {
        userRepository.addVehicle(parameter).subscribe(withLiveData(addVehicleDetailData))
    }

    fun addBankDataApi(parameter: Parameter) {
        userRepository.addBank(parameter).subscribe(withLiveData(bankData))
    }

    fun uploadDocumentApi(driverId: HashMap<String, RequestBody>, pathLicense: String, pathRegistration: String, pathCarFront: String, pathCarBack: String) {
        userRepository.documentImageUpload(driverId, pathLicense, pathRegistration, pathCarFront, pathCarBack)
                .subscribe(withLiveData(uploadDocData))
    }

    fun addDocumentDataApi(parameter: Parameter) {
        userRepository.addDocument(parameter).subscribe(withLiveData(addDocumentData))
    }

    fun updateDocumentApi(parameter: HashMap<String, String>) {
        userRepository.updateDocument(parameter).subscribe(withLiveData(updateDocument))
    }

    fun updateVehicleDocumentApi(parameter: Parameter) {
        userRepository.updateVehicleData(parameter).subscribe(withLiveData(updateVehicleDocument))
    }

    fun updateBankDetailApi(parameter: Parameter) {
        userRepository.updateBankData(parameter).subscribe(withLiveData(updateBankData))
    }

    fun updateDocumentEdit(parameter: Parameter) {
        userRepository.updateDocument(parameter).subscribe(withLiveData(updateDocumentEdit))
    }


}
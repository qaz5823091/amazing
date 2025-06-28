## Amazing Assignment
An app to look up available time of a teacher using Compose, Retrofit, Koin, Coroutines, MockK

## 架構
### Data
#### Model (TeacherTime)
根據 API 回應制定 Model 格式，使用 Gson 解析轉換成 data class

#### ApiService
根據 API method & route 制定規則，並使用 TeacherTime model 來接並用 suspend 掛起這個 function。

因為 API 回傳的時間是字串，無法直接轉換成 LocalDateTime，所以使用 Adapter 塞進 Retrofit 裡面的 GsonFactory，告訴它們轉換成自定義的 Model 規則。

#### Repository (TeacherRepository)
輸入老師的姓名與現在的時間，內部將時間轉換成 API 要的請求時間格式並呼叫，若有錯誤簡單回傳空的 TeacherTime

### Presentation
#### ViewState (MainViewState)
制定畫面中所需要的狀態，包括選擇的老師、第幾週以及可預約的時間

#### ViewModel (MainViewModel)
updateWeek 會依照第 n 週來呼叫 API，selectTeacher 完也會再呼叫 updateWeek

nextWeek 會往下一週呼叫，所以是 updateWeek(1)

previousWeek 會往上一週呼叫，所以是 updateWeek(-1)，最小為第一週 (page = 0)

剩下就是一些資料 format 的部分，想辦法把它塞進 map 裡

#### View (MainScreen)
畫面包含標題、選擇老師的區域、選擇時間的區域

選擇時間包含選擇第 n 週以及選擇一週中的第幾天，往下有那一天可行與被預約的時間

監聽 viewModel 改變的 viewState 去做變化，在呼叫 API 前有改 loading，所以畫面會顯示載入，阻止使用者快速重複點擊

## Done List
- [x] 查看未來的行事曆
- [x] 以週為單位
- [x] 不能查看過去的行事曆
- [x] 在不同國家顯示當地時區的時間
- [x] 行事曆會依照時區顯示可被預約的時間
- [x] 顯示可被預約的時間和已被預約的時間
- [x] 參照後端會提供預約時間的 json 格式，需切割時間區段，每半小時為一個單位
- [x] 已被預約的時間會顯示灰色
- [x] 可被預約的時間會顯示綠色
- [x] 支援 i18n 英文和中文兩種語言
- [ ] Unit Test (完成一半)
- [ ] UI Test 尤佳

## Demo
### 中文
<img src="/demo/tw_screenshot.jpg" width="350px">

### 英文
<img src="/demo/en_screenshot.jpg" width="350px">
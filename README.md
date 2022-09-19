# Uconomy
## Description
경제 플러그인입니다.

### Use Paper Version:
1.18.2
### Tested Paper Version:
1.18.2
### Required Plugin:
- Vault
- PlaceholderAPI (Optional)
## Install Guide
1. 최신 버전의 플러그인 파일을 다운로드합니다.
2. 다운로드한 *.jar 파일을 플러그인 디렉토리에 저장합니다.
## Feature

### 돈 확인 기능
/돈 확인 명령어를 통해 자신의 돈을 확인할 수 있습니다.

### 돈 보내기 기능
/돈 보내기 <닉네임> <금액> 명령어를 통해 <닉네임>에게 <금액>만큼의 돈을 보낼 수 있습니다.

### OP 전용 명령어
#### /돈 확인 <닉네임>
- <닉네임>의 돈을 확인할 수 있습니다.

#### /돈 지급 <닉네임> <금액>
- <닉네임>에게 <금액>만큼의 돈을 지급합니다.

#### /돈 차감 <닉네임> <금액>
- <닉네임>의 돈을 <금액>만큼 차감합니다.

#### /돈 설정 <닉네임> <금액>
- <닉네임>의 돈을 <금액>으로 설정합니다.

### 플러그인 리로드 기능
/uconomy reload 명령어를 사용하여 플러그인을 리로드할 수 있습니다.

### Uconomy Placeholders
- %Uconomy_balance%
  - 해당하는 플레이어의 돈 정보를 불러옵니다.
- %Uconomy_minimum_value% 
  - config.yml의 minimum_amount에 할당된 값을 불러옵니다.
- %Uconomy_transfered_money%
  - /돈 보내기, 지급, 차감, 설정 명령어를 입력하였을 때 플레이어 간의 오고 간 돈의 양을 불러옵니다.
- %Uconomy_sender_name%
  - 돈을 보낸 플레이어의 닉네임을 불러옵니다.
- %Uconomy_recipient_name%
  - 돈을 받은 플레이어의 닉네임을 불러옵니다.

## Commands
```yaml
commands:
  돈:
    permission: ucon.money
  uconomy:
    permission: ucon.reload
```
## Permissions
```yaml
permissions:
  ucon.money:
    default: true
  ucon.reload:
    default: op
  ucon.manage:
    default: op
```
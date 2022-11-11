# Uconomy
## Description
경제 플러그인입니다.

### Use Paper Version:
1.19.2
### Tested Paper Version:
1.19.2
### Required Plugin:
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

### MySQL 저장 방식 지원  
config 파일에서 MySQL 저장 방식을 사용할 것인지 yml 파일 저장 방식을 사용할 것인지 설정할 수 있습니다.

### Uconomy Placeholders
- %uconomy_balance%
  - 해당하는 플레이어의 돈 정보를 불러옵니다.
- %uconomy_minimum_value% 
  - config.yml의 minimum_amount에 할당된 값을 불러옵니다.

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
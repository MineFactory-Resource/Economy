# Uconomy

## Description

경제 플러그인입니다.

### Use Paper Version:

1.20.1

### Tested Purpur Version:

1.20.1

### Required Plugin:

- PlaceholderAPI (Optional)

## Install Guide

1. 최신 버전의 플러그인 파일을 다운로드합니다.
2. 다운로드한 *.jar 파일을 플러그인 디렉토리에 저장합니다.

## Feature

### 돈 종류를 설정해보세요

Uconomy 플러그인에서는 돈을 여러 종류로 설정하여 사용할 수 있습니다.  
해당 기능은 돈과 캐시의 구분 혹은 서버에 여러 게임이 존재할 때, 각 게임의 돈 시스템을 위해서 사용될 수도 있습니다.   
돈 종류는 config.yml의 EconomyID 섹션에서 설정할 수 있습니다.

### 돈 확인 기능

/돈 확인 명령어를 통해 자신의 돈을 확인할 수 있습니다.

### 돈 보내기 기능

/돈 보내기 [닉네임] [EconomyID] [금액] 명령어를 통해 [닉네임]에게 [금액]만큼의 돈을 보낼 수 있습니다.

### OP 전용 명령어

#### /돈 확인 [닉네임]

- [닉네임]의 돈을 확인할 수 있습니다.

#### /돈 지급 [닉네임] [EconomyID] [금액]

- [닉네임]에게 [금액]만큼 [EconomyID] 종류의 돈을 지급합니다.

#### /돈 차감 [닉네임] [EconomyID] [금액]

- [닉네임]의 [EconomyID] 종류의 돈을 [금액]만큼 차감합니다.

#### /돈 설정 [닉네임] [EconomyID] [금액]

- [닉네임]의 [EconomyID] 종류의 돈을 [금액]으로 설정합니다.

### 플러그인 리로드 기능

/uconomy reload 명령어를 사용하여 플러그인을 리로드할 수 있습니다.

### MySQL 저장 방식 지원

config 파일에서 MySQL 저장 방식을 사용할 것인지 yml 파일 저장 방식을 사용할 것인지 설정할 수 있습니다.

### Uconomy Placeholders

- %uconomy_balance_[EconomyID]%
    - 해당하는 플레이어의 [EconomyID] 종류의 돈 정보를 불러옵니다.
    - [EconomyID] 위치에 config.yml의 EconomyID에 설정한 ID를 입력하여 사용할 수 있습니다.  
      Ex) %uconomy_balance_example%
- %uconomy_minimum_value%
    - config.yml의 minimum_amount에 할당된 값을 불러옵니다.

### UconomyAPI
getBalance:  
- 플레이어의 돈 정보를 가져옵니다.  

withdraw:  
- 플레이어의 돈을 차감합니다.  

deposit:  
- 플레이어에게 돈을 지급합니다.  

setBalance:  
- 플레이어의 돈을 설정합니다.  

has:  
- 플레이어가 돈을 충분히 가지고 있는지 확인합니다.

hasAccount:  
- 플레이어의 돈 정보가 데이터베이스에 존재하는지 확인합니다.

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
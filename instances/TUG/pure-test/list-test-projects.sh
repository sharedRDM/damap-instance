#!/usr/bin/env bash
# Overview of the Pure staging test projects: title + participants (with role + person.uuid).
# Local dev/test helper for the Elsevier Pure integration.
# The API key is read from the environment, never stored:  export PURE_API_KEY='...'
set -euo pipefail

BASE="${PURE_BASE_URL:-https://tugraz-staging.elsevierpure.com/ws/api}"
: "${PURE_API_KEY:?export PURE_API_KEY first}"

UUIDS=(
  18fb01c4-5801-468b-8818-43bde001f43d
  0cd8eb67-fa83-469b-9398-4563041305d9
  74b78a48-9498-45c1-87ff-63be5e5410e9
  622713ab-7fa0-4c2b-b916-6e36779f120e
  b9fa4f5f-f6f1-4d24-aa61-1ca3e5d39bfe
  06eb8491-9125-4cc8-88bf-981cf9a34f1a
  d43d4520-daa9-4329-a508-d7372d04ad4f
  03e4e414-570b-4447-b187-19a0468e385c
)

for u in "${UUIDS[@]}"; do
  curl -s -H "api-key: $PURE_API_KEY" "$BASE/projects/$u" \
    | jq -r '"• \(.acronym // "—")  |  \(.title.en_GB // .title.de_DE // "?")",
             (.participants[]? | "    \(.name.firstName) \(.name.lastName)  ->  \(.role.term.en_GB // "?")  [\(.person.uuid // "external")]")'
  echo
done

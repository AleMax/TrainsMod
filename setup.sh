branch=$1

if [ -z "$branch" ]; then
		branch="forge_1.12.2"
fi

rm -rf ./UniversalModCore

git clone --branch $branch git@github.com:TeamOpenIndustry/UniversalModCore.git
